package x10.zenfit.mvp.service.service.userService.getAllUsersUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IUserRepository;
import x10.zenfit.mvp.service.common.domain.entities.user.UserEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetAllUsersUcImpl implements IGetAllUsersUc {

    private final IUserRepository userRepository;

    @Override
    public List<GetAllUsersResponse> getAllUsers() {
        List<UserEntity> users = userRepository.findAll();

        return users.stream()
                .map(user -> GetAllUsersResponse.builder()
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
                        .build())
                .collect(Collectors.toList());
    }

    private boolean canUpdateBirthday(UserEntity user) {
        return user.getBirthdayLastUpdatedAt() == null
                || !user.getBirthdayLastUpdatedAt().plusYears(1).isAfter(LocalDateTime.now());
    }

    private LocalDateTime nextBirthdayUpdateAt(UserEntity user) {
        return user.getBirthdayLastUpdatedAt() == null ? null : user.getBirthdayLastUpdatedAt().plusYears(1);
    }
}
