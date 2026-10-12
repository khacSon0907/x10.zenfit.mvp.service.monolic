package x10.zenfit.mvp.service.service.authService.verifyForgotPasswordOtpUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.config.redis.IRedisService;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.user.UserError;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerifyForgotPasswordOtpUcImpl implements IVerifyForgotPasswordOtpUc {

    private final IRedisService redisService;
    private static final Duration RESET_TOKEN_EXPIRE = Duration.ofMinutes(15);

    @Override
    public VerifyForgotPasswordOtpResponse verifyOtp(VerifyForgotPasswordOtpReq req) {
        String savedOtp = redisService.getForgotPasswordOtp(req.getEmail());

        if (savedOtp == null || !savedOtp.equals(req.getOtp())) {
            throw new BusinessException(UserError.INVALID_OTP);
        }

        redisService.deleteForgotPasswordOtp(req.getEmail());

        String resetToken = UUID.randomUUID().toString();
        redisService.saveResetPasswordToken(req.getEmail(), resetToken, RESET_TOKEN_EXPIRE);

        return VerifyForgotPasswordOtpResponse.builder()
                .resetToken(resetToken)
                .build();
    }
}
