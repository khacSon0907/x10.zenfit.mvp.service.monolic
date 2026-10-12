package x10.zenfit.mvp.service.config.redis;

import java.time.Duration;

public interface IRedisService {

    void saveRegisterOtp(
            String email,
            String otp,
            Duration expiration
    );

    String getRegisterOtp(String email);

    void deleteRegisterOtp(String email);

    void saveForgotPasswordOtp(
            String email,
            String otp,
            Duration expiration
    );

    String getForgotPasswordOtp(String email);

    void deleteForgotPasswordOtp(String email);

    void saveForgotPasswordCooldown(String email, Duration duration);

    boolean isForgotPasswordCooldown(String email);

    void saveResetPasswordToken(String email, String token, Duration duration);

    String getResetPasswordToken(String email);

    void deleteResetPasswordToken(String email);

    long incrementFailedLogin(String email);

    void resetFailedLogin(String email);

    void lockUserLogin(String email, Duration duration);

    boolean isLoginLocked(String email);

    void addToBlacklist(String token, long expirationInMs);

    boolean isTokenBlacklisted(String token);

    void saveOAuth2LoginCode(String code, String payload, Duration expiration);

    String getOAuth2LoginCode(String code);

    void deleteOAuth2LoginCode(String code);
}
