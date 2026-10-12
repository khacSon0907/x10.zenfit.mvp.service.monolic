package x10.zenfit.mvp.service.service.userService.updateUserUc;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IUserBirthdayHistoryRepository;
import x10.zenfit.mvp.service.Repository.IUserRepository;
import x10.zenfit.mvp.service.common.domain.entities.user.UserBirthdayHistoryEntity;
import x10.zenfit.mvp.service.common.domain.entities.user.UserEntity;
import x10.zenfit.mvp.service.common.domain.enums.UserStatus;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.CommonError;
import x10.zenfit.mvp.service.share.exception.user.UserError;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@AllArgsConstructor
public class UpdateUserUcImpl implements IUpdateUserUc {

    private final IUserRepository userRepository;
    private final IUserBirthdayHistoryRepository birthdayHistoryRepository;

    @Override
    public UpdateUserResponse updateUser(String userId, UpdateUserRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));
        updateUserFields(user, request);
        updateBirthday(user, userId, request.getBirthday(), false);
        user.setUpdatedAt(Instant.now());
        return toResponse(userRepository.save(user));
    }

    @Override
    public UpdateUserResponse updateUserByAdmin(String userId, String adminId, UpdateUserRequest request) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));
        updateUserFields(user, request);
        updateBirthday(user, adminId, request.getBirthday(), true);
        user.setUpdatedAt(Instant.now());
        return toResponse(userRepository.save(user));
    }

    @Override
    public UpdateUserResponse updateUserStatusByAdmin(String userId, String adminId, boolean locked) {
        if (userId.equals(adminId)) {
            throw new BusinessException(CommonError.INVALID_REQUEST, "Admin khong the khoa tai khoan cua chinh minh");
        }

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));

        user.setStatus(locked ? UserStatus.BLOCKED : UserStatus.ACTIVE);
        user.setUpdatedAt(Instant.now());

        return toResponse(userRepository.save(user));
    }

    private void updateUserFields(UserEntity user, UpdateUserRequest request) {
        if (request.getUsername() != null && !request.getUsername().isEmpty()) {
            user.setUsername(request.getUsername());
        }
        if (request.getPhoneNumber() != null && !request.getPhoneNumber().isEmpty()) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getAvatarUrl() != null && !request.getAvatarUrl().isEmpty()) {
            user.setAvatarUrl(request.getAvatarUrl());
        }
    }

    private void updateBirthday(UserEntity user, String changedByUserId, LocalDate birthday, boolean changedByAdmin) {
        if (birthday == null || birthday.equals(user.getBirthday())) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        if (!changedByAdmin && !canUpdateBirthday(user, now)) {
            throw new BusinessException(
                    UserError.BIRTHDAY_UPDATE_LIMIT,
                    "Bạn chỉ được thay đổi ngày sinh 1 lần mỗi năm. Nếu cần thay đổi thêm, vui lòng liên hệ bộ phận Chăm sóc khách hàng."
            );
        }

        LocalDate oldBirthday = user.getBirthday();
        user.setBirthday(birthday);
        user.setBirthdayLastUpdatedAt(now);
        user.setBirthdayUpdateCount(defaultBirthdayUpdateCount(user) + 1);

        birthdayHistoryRepository.save(UserBirthdayHistoryEntity.builder()
                .id(UUID.randomUUID().toString())
                .userId(user.getId())
                .oldBirthday(oldBirthday)
                .newBirthday(birthday)
                .changedByUserId(changedByUserId)
                .changedByAdmin(changedByAdmin)
                .changedAt(now)
                .build());
    }

    private UpdateUserResponse toResponse(UserEntity updatedUser) {
        return UpdateUserResponse.builder()
                .id(updatedUser.getId())
                .username(updatedUser.getUsername())
                .email(updatedUser.getEmail())
                .phoneNumber(updatedUser.getPhoneNumber())
                .status(updatedUser.getStatus())
                .createdAt(updatedUser.getCreatedAt())
                .updatedAt(updatedUser.getUpdatedAt())
                .emailVerified(updatedUser.isEmailVerified())
                .verifiedAt(updatedUser.getVerifiedAt())
                .avatarUrl(updatedUser.getAvatarUrl())
                .birthday(updatedUser.getBirthday())
                .birthdayUpdateCount(defaultBirthdayUpdateCount(updatedUser))
                .points(updatedUser.getPoints() == null ? 0L : updatedUser.getPoints())
                .birthdayLastUpdatedAt(updatedUser.getBirthdayLastUpdatedAt())
                .nextBirthdayUpdateAt(nextBirthdayUpdateAt(updatedUser))
                .canUpdateBirthday(canUpdateBirthday(updatedUser, LocalDateTime.now()))
                .providerType(updatedUser.getProviderType())
                .roleIds(updatedUser.getRoleIds())
                .build();
    }

    private boolean canUpdateBirthday(UserEntity user, LocalDateTime now) {
        return user.getBirthdayLastUpdatedAt() == null
                || !user.getBirthdayLastUpdatedAt().plusYears(1).isAfter(now);
    }

    private LocalDateTime nextBirthdayUpdateAt(UserEntity user) {
        return user.getBirthdayLastUpdatedAt() == null ? null : user.getBirthdayLastUpdatedAt().plusYears(1);
    }

    private int defaultBirthdayUpdateCount(UserEntity user) {
        return user.getBirthdayUpdateCount() == null ? 0 : user.getBirthdayUpdateCount();
    }
}
