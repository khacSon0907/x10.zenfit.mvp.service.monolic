package x10.zenfit.mvp.service.service.authService.oauth2LoginUc;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.config.redis.IRedisService;
import x10.zenfit.mvp.service.service.authService.loginUc.LoginResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.user.UserError;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OAuth2LoginCodeService {

    private static final Duration CODE_EXPIRATION = Duration.ofMinutes(2);

    private final IRedisService redisService;
    private final ObjectMapper objectMapper;

    public String createCode(LoginResponse loginResponse) {
        try {
            String code = UUID.randomUUID().toString();
            OAuth2LoginCodePayload payload = OAuth2LoginCodePayload.from(loginResponse);

            redisService.saveOAuth2LoginCode(
                    code,
                    objectMapper.writeValueAsString(payload),
                    CODE_EXPIRATION
            );

            return code;
        } catch (Exception e) {
            throw new BusinessException(UserError.INVALID_OAUTH2_CODE);
        }
    }

    public LoginResponse consumeCode(String code) {
        if (code == null || code.trim().isEmpty()) {
            throw new BusinessException(UserError.INVALID_OAUTH2_CODE);
        }

        String payloadValue = redisService.getOAuth2LoginCode(code);
        redisService.deleteOAuth2LoginCode(code);

        if (payloadValue == null || payloadValue.trim().isEmpty()) {
            throw new BusinessException(UserError.INVALID_OAUTH2_CODE);
        }

        try {
            OAuth2LoginCodePayload payload = objectMapper.readValue(
                    payloadValue,
                    OAuth2LoginCodePayload.class
            );

            return payload.toLoginResponse();
        } catch (Exception e) {
            throw new BusinessException(UserError.INVALID_OAUTH2_CODE);
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    static class OAuth2LoginCodePayload {
        private String id;
        private String username;
        private String email;
        private List<String> roleIds;
        private List<String> roles;
        private String accessToken;
        private String refreshToken;

        private static OAuth2LoginCodePayload from(LoginResponse loginResponse) {
            return new OAuth2LoginCodePayload(
                    loginResponse.getId(),
                    loginResponse.getUsername(),
                    loginResponse.getEmail(),
                    loginResponse.getRoleIds(),
                    loginResponse.getRoles(),
                    loginResponse.getAccessToken(),
                    loginResponse.getRefreshToken()
            );
        }

        private LoginResponse toLoginResponse() {
            return LoginResponse.builder()
                    .id(id)
                    .username(username)
                    .email(email)
                    .roleIds(roleIds)
                    .roles(roles)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
    }
}
