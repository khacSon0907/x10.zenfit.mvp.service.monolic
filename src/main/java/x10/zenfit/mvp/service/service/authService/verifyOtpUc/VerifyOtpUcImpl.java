package x10.zenfit.mvp.service.service.authService.verifyOtpUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IUserRepository;
import x10.zenfit.mvp.service.common.domain.entities.user.UserEntity;
import x10.zenfit.mvp.service.common.domain.enums.UserStatus;
import x10.zenfit.mvp.service.config.redis.IRedisService;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.user.UserError;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VerifyOtpUcImpl implements IVerifyOtpUc {

    private final IRedisService redisService;
    private final IUserRepository userRepository;

    @Override
    public void verifyOtp(VerifyOtpReq req) {
        String savedOtp = redisService.getRegisterOtp(req.getEmail());

        if (savedOtp == null || !savedOtp.equals(req.getOtp())) {
            throw new BusinessException(UserError.INVALID_OTP);
        }

        UserEntity user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));

        user.setEmailVerified(true);
        user.setStatus(UserStatus.ACTIVE);
        user.setVerifiedAt(LocalDateTime.now());

        userRepository.save(user);

        redisService.deleteRegisterOtp(req.getEmail());
    }
}
