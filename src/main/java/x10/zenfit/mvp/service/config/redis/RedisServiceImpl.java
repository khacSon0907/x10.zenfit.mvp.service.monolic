package x10.zenfit.mvp.service.config.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisServiceImpl implements IRedisService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String REGISTER_OTP_PREFIX = "register_otp:";

    @Override
    public void saveRegisterOtp(
            String email,
            String otp,
            Duration expiration
    ) {
        try {
            String key = REGISTER_OTP_PREFIX + email;

            redisTemplate.opsForValue().set(
                    key,
                    otp,
                    expiration
            );

            log.info("OTP saved to Redis: {}", key);

        } catch (Exception e) {
            log.error(
                    "Error saving register OTP for email: {}",
                    email,
                    e
            );
        }
    }

    @Override
    public String getRegisterOtp(String email) {
        try {
            return redisTemplate.opsForValue().get(
                    REGISTER_OTP_PREFIX + email
            );
        } catch (Exception e) {
            log.error(
                    "Error getting register OTP for email: {}",
                    email,
                    e
            );
            return null;
        }
    }

    @Override
    public void deleteRegisterOtp(String email) {
        try {
            redisTemplate.delete(
                    REGISTER_OTP_PREFIX + email
            );
        } catch (Exception e) {
            log.error(
                    "Error deleting register OTP for email: {}",
                    email,
                    e
            );
        }
    }

    private static final String FORGOT_PASSWORD_OTP_PREFIX = "forgot_password_otp:";

    @Override
    public void saveForgotPasswordOtp(String email, String otp, Duration expiration) {
        try {
            String key = FORGOT_PASSWORD_OTP_PREFIX + email;
            redisTemplate.opsForValue().set(key, otp, expiration);
            log.info("Forgot Password OTP saved to Redis: {}", key);
        } catch (Exception e) {
            log.error("Error saving forgot password OTP for email: {}", email, e);
        }
    }

    @Override
    public String getForgotPasswordOtp(String email) {
        try {
            return redisTemplate.opsForValue().get(FORGOT_PASSWORD_OTP_PREFIX + email);
        } catch (Exception e) {
            log.error("Error getting forgot password OTP for email: {}", email, e);
            return null;
        }
    }

    @Override
    public void deleteForgotPasswordOtp(String email) {
        try {
            redisTemplate.delete(FORGOT_PASSWORD_OTP_PREFIX + email);
        } catch (Exception e) {
            log.error("Error deleting forgot password OTP for email: {}", email, e);
        }
    }

    private static final String FORGOT_PASSWORD_COOLDOWN_PREFIX = "forgot_password_cooldown:";
    private static final String RESET_PASSWORD_TOKEN_PREFIX = "reset_password_token:";

    @Override
    public void saveForgotPasswordCooldown(String email, Duration duration) {
        try {
            String key = FORGOT_PASSWORD_COOLDOWN_PREFIX + email;
            redisTemplate.opsForValue().set(key, "cooldown", duration);
        } catch (Exception e) {
            log.error("Error saving forgot password cooldown for email: {}", email, e);
        }
    }

    @Override
    public boolean isForgotPasswordCooldown(String email) {
        try {
            String key = FORGOT_PASSWORD_COOLDOWN_PREFIX + email;
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error("Error checking forgot password cooldown for email: {}", email, e);
            return false;
        }
    }

    @Override
    public void saveResetPasswordToken(String email, String token, Duration duration) {
        try {
            String key = RESET_PASSWORD_TOKEN_PREFIX + email;
            redisTemplate.opsForValue().set(key, token, duration);
        } catch (Exception e) {
            log.error("Error saving reset password token for email: {}", email, e);
        }
    }

    @Override
    public String getResetPasswordToken(String email) {
        try {
            return redisTemplate.opsForValue().get(RESET_PASSWORD_TOKEN_PREFIX + email);
        } catch (Exception e) {
            log.error("Error getting reset password token for email: {}", email, e);
            return null;
        }
    }

    @Override
    public void deleteResetPasswordToken(String email) {
        try {
            redisTemplate.delete(RESET_PASSWORD_TOKEN_PREFIX + email);
        } catch (Exception e) {
            log.error("Error deleting reset password token for email: {}", email, e);
        }
    }

    private static final String LOGIN_FAILED_PREFIX = "login_failed:";
    private static final String LOGIN_LOCKED_PREFIX = "login_locked:";

    @Override
    public long incrementFailedLogin(String email) {
        try {
            String key = LOGIN_FAILED_PREFIX + email;
            Long count = redisTemplate.opsForValue().increment(key);
            if (count != null && count == 1) {
                // Set expiration for 1 hour so the counter resets eventually
                redisTemplate.expire(key, Duration.ofHours(1));
            }
            return count != null ? count : 0;
        } catch (Exception e) {
            log.error("Error incrementing failed login for email: {}", email, e);
            return 0;
        }
    }

    @Override
    public void resetFailedLogin(String email) {
        try {
            redisTemplate.delete(LOGIN_FAILED_PREFIX + email);
        } catch (Exception e) {
            log.error("Error resetting failed login for email: {}", email, e);
        }
    }

    @Override
    public void lockUserLogin(String email, Duration duration) {
        try {
            String key = LOGIN_LOCKED_PREFIX + email;
            redisTemplate.opsForValue().set(key, "locked", duration);
            log.info("User {} locked for {}", email, duration);
        } catch (Exception e) {
            log.error("Error locking user login for email: {}", email, e);
        }
    }

    @Override
    public boolean isLoginLocked(String email) {
        try {
            String key = LOGIN_LOCKED_PREFIX + email;
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error("Error checking login locked status for email: {}", email, e);
            return false;
        }
    }

    private static final String BLACKLIST_PREFIX = "token_blacklist:";
    private static final String OAUTH2_LOGIN_CODE_PREFIX = "oauth2_login_code:";

    @Override
    public void addToBlacklist(String token, long expirationInMs) {
        try {
            if (expirationInMs > 0) {
                String key = BLACKLIST_PREFIX + token;
                redisTemplate.opsForValue().set(key, "blacklisted", Duration.ofMillis(expirationInMs));
                log.info("Token added to blacklist with expiration: {} ms", expirationInMs);
            }
        } catch (Exception e) {
            log.error("Error adding token to blacklist", e);
        }
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        try {
            String key = BLACKLIST_PREFIX + token;
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.error("Error checking token in blacklist", e);
            return false;
        }
    }

    @Override
    public void saveOAuth2LoginCode(String code, String payload, Duration expiration) {
        try {
            String key = OAUTH2_LOGIN_CODE_PREFIX + code;
            redisTemplate.opsForValue().set(key, payload, expiration);
        } catch (Exception e) {
            log.error("Error saving OAuth2 login code", e);
        }
    }

    @Override
    public String getOAuth2LoginCode(String code) {
        try {
            String key = OAUTH2_LOGIN_CODE_PREFIX + code;
            return redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Error getting OAuth2 login code", e);
            return null;
        }
    }

    @Override
    public void deleteOAuth2LoginCode(String code) {
        try {
            String key = OAUTH2_LOGIN_CODE_PREFIX + code;
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Error deleting OAuth2 login code", e);
        }
    }
}
