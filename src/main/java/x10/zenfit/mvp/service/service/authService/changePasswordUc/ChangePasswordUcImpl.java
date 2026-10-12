package x10.zenfit.mvp.service.service.authService.changePasswordUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IUserRepository;
import x10.zenfit.mvp.service.common.domain.entities.user.UserEntity;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.user.UserError;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChangePasswordUcImpl implements IChangePasswordUc {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void changePassword(String userId, ChangePasswordReq req) {
        // 1. Validate request
        if (!req.getNewPassword().equals(req.getConfirmPassword())) {
            throw new BusinessException(UserError.PASSWORD_MISMATCH);
        }

        // 2. Check if new password is the same as current password
        if (req.getCurrentPassword().equals(req.getNewPassword())) {
            throw new BusinessException(UserError.SAME_PASSWORD);
        }

        // 3. Find user by ID
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));

        // 4. Verify current password
        if (!passwordEncoder.matches(req.getCurrentPassword(), user.getPasswordHash())) {
            throw new BusinessException(UserError.INVALID_CREDENTIALS);
        }

        // 5. Update password
        user.setPasswordHash(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);

        log.info("User {} changed password successfully", userId);
    }
}

