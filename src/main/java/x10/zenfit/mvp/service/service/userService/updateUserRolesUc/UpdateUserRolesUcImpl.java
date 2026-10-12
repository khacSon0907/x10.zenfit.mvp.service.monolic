package x10.zenfit.mvp.service.service.userService.updateUserRolesUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IRoleRepository;
import x10.zenfit.mvp.service.Repository.IUserRepository;
import x10.zenfit.mvp.service.common.domain.entities.user.RoleEntity;
import x10.zenfit.mvp.service.common.domain.entities.user.UserEntity;
import x10.zenfit.mvp.service.service.userService.updateUserUc.UpdateUserResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.role.RoleError;
import x10.zenfit.mvp.service.share.exception.user.UserError;

import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdateUserRolesUcImpl implements IUpdateUserRolesUc {

    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;

    @Override
    public UpdateUserResponse updateUserRoles(String userId, List<String> roleIds) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));

        user.setRoleIds(validateRoleIds(roleIds));
        user.setUpdatedAt(Instant.now());

        UserEntity updatedUser = userRepository.save(user);

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
                .providerType(updatedUser.getProviderType())
                .roleIds(updatedUser.getRoleIds())
                .build();
    }

    private List<String> validateRoleIds(List<String> roleIds) {

        if (roleIds == null) {
            throw new BusinessException(RoleError.ROLE_NOT_FOUND);
        }

        List<String> normalizedRoleIds = roleIds.stream()
                .map(String::trim)
                .filter(roleId -> !roleId.isEmpty())
                .distinct()
                .toList();

        if (normalizedRoleIds.isEmpty()) {
            throw new BusinessException(RoleError.ROLE_NOT_FOUND);
        }

        List<RoleEntity> roles = roleRepository.findAllById(normalizedRoleIds);

        if (roles.size() != normalizedRoleIds.size() || roles.stream().anyMatch(role -> !role.isActive())) {
            throw new BusinessException(RoleError.ROLE_NOT_FOUND);
        }

        return normalizedRoleIds;
    }
}
