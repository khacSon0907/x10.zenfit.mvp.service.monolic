package x10.zenfit.mvp.service.service.authService.oauth2LoginUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IUserRepository;
import x10.zenfit.mvp.service.common.domain.dto.request.TokenPayload;
import x10.zenfit.mvp.service.common.domain.entities.user.UserEntity;
import x10.zenfit.mvp.service.common.domain.enums.AuthProvider;
import x10.zenfit.mvp.service.common.domain.enums.UserStatus;
import x10.zenfit.mvp.service.config.jwt.IJwtService;
import x10.zenfit.mvp.service.service.authService.loginUc.LoginResponse;
import x10.zenfit.mvp.service.service.roleService.RoleResolver;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.user.UserError;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoogleOauth2ServiceImpl implements GoogleOauth2Service {

    private final IUserRepository userRepository;
    private final IJwtService jwtService;
    private final RoleResolver roleResolver;

    @Override
    public LoginResponse loginWithGoogle(OAuth2User oauth2User) {

        log.info("Starting Google OAuth2 login process from OAuth2User");

        GoogleUserInfo googleUserInfo = extractGoogleUserInfo(oauth2User);

        log.info("Google user info extracted successfully for email: {}", googleUserInfo.getEmail());

        UserEntity user = userRepository.findByEmail(googleUserInfo.getEmail())
                .orElseGet(() -> {
                    log.info("User not found, creating new Google user with email: {}", googleUserInfo.getEmail());
                    return createNewGoogleUser(googleUserInfo);
                });

        // Nếu user cũ chưa verify email, Google đã verify rồi thì update luôn
        if (!user.isEmailVerified()) {
            user.setEmailVerified(true);
            user.setVerifiedAt(LocalDateTime.now());
        }

        // Update provider type khi login Google
        updateProviderTypeForGoogleLogin(user);

        // Nếu user chưa có avatar thì lấy avatar từ Google
        if (user.getAvatarUrl() == null || user.getAvatarUrl().isBlank()) {
            user.setAvatarUrl(googleUserInfo.getPicture());
        }

        // Nếu username bị null/blank thì tạo từ email
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            user.setUsername(generateUsernameFromEmail(user.getEmail()));
        }

        // Nếu roles bị null/rỗng thì set role mặc định (PT)
        if (user.getRoleIds() == null || user.getRoleIds().isEmpty()) {
            user.setRoleIds(roleResolver.getDefaultRoleIds());
        }

        user.setUpdatedAt(Instant.now());
        user = userRepository.save(user);

        List<String> roleIds = normalizeRoleIds(user.getRoleIds());
        List<String> roles = roleResolver.resolveRoleCodes(roleIds);

        TokenPayload tokenPayload = TokenPayload.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roleIds(roleIds)
                .roles(roles)
                .build();

        String accessToken = jwtService.generateAccessToken(tokenPayload);
        String refreshToken = jwtService.generateRefreshToken(tokenPayload);

        log.info("Google login success. Tokens generated for user: {}", user.getId());

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

    private void updateProviderTypeForGoogleLogin(UserEntity user) {
        AuthProvider providerType = user.getProviderType();

        if (providerType == null) {
            user.setProviderType(AuthProvider.GOOGLE);
            return;
        }

        if (providerType == AuthProvider.LOCAL) {
            user.setProviderType(AuthProvider.LOCAL_GOOGLE);
            return;
        }

        // Nếu đã là GOOGLE hoặc LOCAL_GOOGLE thì giữ nguyên
        if (providerType == AuthProvider.GOOGLE || providerType == AuthProvider.LOCAL_GOOGLE) {
            return;
        }

        // Nếu là GUEST mà login Google thì chuyển thành GOOGLE
        if (providerType == AuthProvider.GUEST) {
            user.setProviderType(AuthProvider.GOOGLE);
        }
    }

    private GoogleUserInfo extractGoogleUserInfo(OAuth2User oauth2User) {

        String email = oauth2User.getAttribute("email");
        String name = oauth2User.getAttribute("name");
        String picture = oauth2User.getAttribute("picture");
        String googleId = oauth2User.getAttribute("sub");
        Boolean emailVerified = oauth2User.getAttribute("email_verified");

        if (email == null || email.isBlank()) {
            log.error("Google OAuth2 email is missing");
            throw new BusinessException(UserError.INVALID_GOOGLE_TOKEN);
        }

        if (emailVerified == null || !emailVerified) {
            log.warn("Google email is not verified: {}", email);
            throw new BusinessException(UserError.EMAIL_NOT_VERIFIED_GOOGLE);
        }

        return GoogleUserInfo.builder()
                .email(email)
                .name(name)
                .picture(picture)
                .googleId(googleId)
                .emailVerified(emailVerified)
                .build();
    }

    private UserEntity createNewGoogleUser(GoogleUserInfo googleUserInfo) {

        String userId = UUID.randomUUID().toString();
        String username = generateUsernameFromEmail(googleUserInfo.getEmail());

        UserEntity newUser = UserEntity.builder()
                .id(userId)
                .username(username)
                .email(googleUserInfo.getEmail())
                .passwordHash(null)
                .status(UserStatus.ACTIVE)
                .emailVerified(true)
                .verifiedAt(LocalDateTime.now())
                .avatarUrl(googleUserInfo.getPicture())
                .providerType(AuthProvider.GOOGLE)
                .points(0L)
                .roleIds(roleResolver.getDefaultRoleIds())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        UserEntity savedUser = userRepository.save(newUser);

        log.info("New Google user created with id: {}", savedUser.getId());

        return savedUser;
    }

    private String generateUsernameFromEmail(String email) {
        return email.split("@")[0];
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
