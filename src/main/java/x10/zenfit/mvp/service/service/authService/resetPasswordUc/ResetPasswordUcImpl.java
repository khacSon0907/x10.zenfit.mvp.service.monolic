package x10.zenfit.mvp.service.service.authService.resetPasswordUc;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IUserRepository;
import x10.zenfit.mvp.service.common.domain.entities.user.UserEntity;
import x10.zenfit.mvp.service.config.redis.IRedisService;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.user.UserError;

@Service
@RequiredArgsConstructor
public class ResetPasswordUcImpl implements IResetPasswordUc {

    private final IUserRepository userRepository;
    private final IRedisService redisService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void resetPassword(ResetPasswordReq req) {
        String savedToken = redisService.getResetPasswordToken(req.getEmail());

        if (savedToken == null || !savedToken.equals(req.getResetToken())) {
            throw new BusinessException(UserError.INVALID_RESET_TOKEN);
        }

        UserEntity user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));

        user.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);

        redisService.deleteResetPasswordToken(req.getEmail());
    }
}
