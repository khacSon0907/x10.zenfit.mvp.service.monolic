package x10.zenfit.mvp.service.service.authService.loginUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IUserRepository;
import x10.zenfit.mvp.service.common.domain.dto.request.TokenPayload;
import x10.zenfit.mvp.service.common.domain.entities.user.UserEntity;
import x10.zenfit.mvp.service.common.domain.enums.AuthProvider;
import x10.zenfit.mvp.service.common.domain.enums.UserStatus;
import x10.zenfit.mvp.service.config.jwt.IJwtService;
import x10.zenfit.mvp.service.config.redis.IRedisService;
import x10.zenfit.mvp.service.service.roleService.RoleResolver;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.user.UserError;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginUcImpl implements ILoginUc {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IRedisService redisService;
    private final IJwtService jwtService;
    private final RoleResolver roleResolver;

    private static final int MAX_FAILED_ATTEMPTS = 5;
    private static final Duration LOCK_DURATION = Duration.ofMinutes(5);

    @Override
    public LoginResponse login(LoginReq req) {

        String email = req.getEmail().trim().toLowerCase();

        // 1. Check login lock
        if (redisService.isLoginLocked(email)) {
            throw new BusinessException(UserError.LOGIN_LOCKED);
        }

        // 2. Find user
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));

        // 3. Check provider type
        validateProviderCanLoginWithPassword(user);

        // 4. Verify password
        if (user.getPasswordHash() == null
                || user.getPasswordHash().isBlank()
                || !passwordEncoder.matches(req.getPassword(), user.getPasswordHash())) {

            long failedCount = redisService.incrementFailedLogin(email);

            log.warn("Failed login attempt {} for user {}", failedCount, email);

            if (failedCount >= MAX_FAILED_ATTEMPTS) {
                redisService.lockUserLogin(email, LOCK_DURATION);
                redisService.resetFailedLogin(email);

                throw new BusinessException(UserError.LOGIN_LOCKED);
            }

            throw new BusinessException(UserError.INVALID_CREDENTIALS);
        }

        // 5. Reset failed login
        redisService.resetFailedLogin(email);

        // 6. Check email verified
        if (!user.isEmailVerified()) {
            throw new BusinessException(UserError.EMAIL_NOT_VERIFIED);
        }

        // 7. Check user status
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException(UserError.USER_NOT_ACTIVE);
        }

        // 8. Build roles
        List<String> roleIds = normalizeRoleIds(user.getRoleIds());
        List<String> roles = roleResolver.resolveRoleCodes(roleIds);

        // 9. Build token payload
        TokenPayload payload = TokenPayload.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roleIds(roleIds)
                .roles(roles)
                .build();

        // 10. Generate tokens
        String accessToken = jwtService.generateAccessToken(payload);
        String refreshToken = jwtService.generateRefreshToken(payload);

        // 11. Return response
        return LoginResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roleIds(roleIds)
                .roles(roles)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void validateProviderCanLoginWithPassword(UserEntity user) {
        AuthProvider providerType = user.getProviderType();

        if (providerType == AuthProvider.GUEST) {
            throw new BusinessException(UserError.GUEST_ACCOUNT_CANNOT_LOGIN);
        }

        if (providerType == AuthProvider.GOOGLE
                && (user.getPasswordHash() == null || user.getPasswordHash().isBlank())) {
            throw new BusinessException(UserError.GOOGLE_ACCOUNT_LOGIN_REQUIRED);
        }

        // LOCAL: cho login bằng email/password
        // LOCAL_GOOGLE: cho login bằng email/password và Google
        // null: cho đi tiếp để tương thích data cũ
    }

    private List<String> normalizeRoleIds(List<String> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return roleResolver.getDefaultRoleIds();
        }

        List<String> normalizedRoleIds = roleIds.stream()
                .map(String::trim)
                .filter(roleId -> !roleId.isEmpty())
                .toList();

        return normalizedRoleIds.isEmpty() ? roleResolver.getDefaultRoleIds() : normalizedRoleIds;
    }
}
