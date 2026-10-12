package x10.zenfit.mvp.service.service.authService.refreshTokenUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IUserRepository;
import x10.zenfit.mvp.service.common.domain.dto.request.TokenPayload;
import x10.zenfit.mvp.service.common.domain.entities.user.UserEntity;
import x10.zenfit.mvp.service.common.domain.enums.UserStatus;
import x10.zenfit.mvp.service.config.jwt.IJwtService;
import x10.zenfit.mvp.service.config.redis.IRedisService;
import x10.zenfit.mvp.service.service.authService.loginUc.LoginResponse;
import x10.zenfit.mvp.service.service.roleService.RoleResolver;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.user.UserError;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenUcImpl implements IRefreshTokenUc {

    private final IUserRepository userRepository;
    private final IRedisService redisService;
    private final IJwtService jwtService;
    private final RoleResolver roleResolver;

    @Override
    public LoginResponse refreshToken(String refreshToken) {
        // 1. Kiểm tra refresh token có trống hay không
        if (refreshToken == null || refreshToken.trim().isEmpty()) {
            throw new BusinessException(UserError.INVALID_REFRESH_TOKEN);
        }

        // 2. Kiểm tra xem token có nằm trong blacklist của Redis không
        if (redisService.isTokenBlacklisted(refreshToken)) {
            log.warn("Cảnh báo: Phát hiện nỗ lực sử dụng lại Refresh Token đã bị blacklist");
            throw new BusinessException(UserError.INVALID_REFRESH_TOKEN);
        }

        // 3. Xác thực Refresh Token và giải mã payload
        TokenPayload payload;
        try {
            payload = jwtService.validateRefreshToken(refreshToken);
        } catch (Exception e) {
            log.error("Xác thực Refresh Token thất bại: {}", e.getMessage());
            throw new BusinessException(UserError.INVALID_REFRESH_TOKEN);
        }

        // 4. Tìm kiếm người dùng bằng email trong payload
        UserEntity user = userRepository.findByEmail(payload.getEmail())
                .orElseThrow(() -> {
                    log.error("Không tìm thấy người dùng với email trong token: {}", payload.getEmail());
                    return new BusinessException(UserError.USER_NOT_FOUND);
                });

        // 5. Kiểm tra trạng thái kích hoạt email của người dùng
        if (!user.isEmailVerified()) {
            throw new BusinessException(UserError.EMAIL_NOT_VERIFIED);
        }

        // 6. Kiểm tra xem tài khoản người dùng có đang hoạt động hay không
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException(UserError.USER_NOT_ACTIVE);
        }

        // 7. Tạo Access Token mới và Refresh Token mới (Xoay vòng token)
        // Prefer roles from user entity (more authoritative). If not available, use roles from token payload, else default to USER.
        List<String> roleIds = normalizeRoleIds(user.getRoleIds());
        List<String> roles = roleResolver.resolveRoleCodes(roleIds);

        if ((user.getRoleIds() == null || user.getRoleIds().isEmpty())
                && payload.getRoles() != null
                && !payload.getRoles().isEmpty()) {
            roles = normalizeRoles(payload.getRoles());
        }

        TokenPayload newPayload = TokenPayload.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roleIds(roleIds)
                .roles(roles)
                .build();

        String newAccessToken = jwtService.generateAccessToken(newPayload);
        String newRefreshToken = jwtService.generateRefreshToken(newPayload);

        // 8. Đưa Refresh Token cũ vào blacklist của Redis để ngăn chặn replay attack
        try {
            long remainingExpiration = jwtService.getRemainingExpiration(refreshToken);
            if (remainingExpiration > 0) {
                redisService.addToBlacklist(refreshToken, remainingExpiration);
                log.info("Refresh Token cũ đã được thêm vào blacklist thành công. Hạn dùng còn lại: {} ms", remainingExpiration);
            }
        } catch (Exception e) {
            log.warn("Không thể lấy thời gian hết hạn còn lại hoặc blacklist Refresh Token cũ: {}", e.getMessage());
        }

        // 9. Trả về LoginResponse
        return LoginResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .roleIds(roleIds)
                .roles(roles)
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    private List<String> normalizeRoles(List<String> roles) {
        List<String> normalizedRoles = roles.stream()
                .map(String::trim)
                .filter(role -> !role.isEmpty())
                .toList();

        return normalizedRoles.isEmpty() ? List.of("USER") : normalizedRoles;
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
