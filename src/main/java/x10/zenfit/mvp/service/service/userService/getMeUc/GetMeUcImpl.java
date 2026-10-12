package x10.zenfit.mvp.service.service.userService.getMeUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IUserRepository;
import x10.zenfit.mvp.service.common.domain.entities.user.UserEntity;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.user.UserError;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class GetMeUcImpl implements IGetMeUc {

    private final IUserRepository userRepository;

    @Override
    public GetMeResponse getMe(String userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));

        return GetMeResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .emailVerified(user.isEmailVerified())
                .verifiedAt(user.getVerifiedAt())
                .avatarUrl(user.getAvatarUrl())
                .birthday(user.getBirthday())
                .birthdayUpdateCount(user.getBirthdayUpdateCount() == null ? 0 : user.getBirthdayUpdateCount())
                .points(user.getPoints() == null ? 0L : user.getPoints())
                .birthdayLastUpdatedAt(user.getBirthdayLastUpdatedAt())
                .nextBirthdayUpdateAt(nextBirthdayUpdateAt(user))
                .canUpdateBirthday(canUpdateBirthday(user))
                .providerType(user.getProviderType())
                .roleIds(user.getRoleIds())
                .build();
    }

    private boolean canUpdateBirthday(UserEntity user) {
        return user.getBirthdayLastUpdatedAt() == null
                || !user.getBirthdayLastUpdatedAt().plusYears(1).isAfter(LocalDateTime.now());
    }

    private LocalDateTime nextBirthdayUpdateAt(UserEntity user) {
        return user.getBirthdayLastUpdatedAt() == null ? null : user.getBirthdayLastUpdatedAt().plusYears(1);
    }
}
