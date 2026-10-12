package x10.zenfit.mvp.service.infrastructure.user.adapter.mapper;

import x10.zenfit.mvp.service.common.domain.entities.user.UserEntity;
import x10.zenfit.mvp.service.infrastructure.user.db.mongodb.UserDocument;

public class UserMapper {

    public static UserEntity toEntity(UserDocument document) {

        if (document == null) {
            return null;
        }

        return UserEntity.builder()
                .id(document.getId())
                .username(document.getUsername())
                .email(document.getEmail())
                .passwordHash(document.getPasswordHash())
                .phoneNumber(document.getPhoneNumber())
                .status(document.getStatus())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .emailVerified(document.isEmailVerified())
                .verifiedAt(document.getVerifiedAt())
                .avatarUrl(document.getAvatarUrl())
                .birthday(document.getBirthday())
                .birthdayLastUpdatedAt(document.getBirthdayLastUpdatedAt())
                .birthdayUpdateCount(document.getBirthdayUpdateCount() == null ? 0 : document.getBirthdayUpdateCount())
                .points(document.getPoints() == null ? 0L : document.getPoints())
                .providerType(document.getProviderType())

                // ROLE
                .roleIds(document.getRoleIds())

                .build();
    }

    public static UserDocument toDocument(UserEntity entity) {

        if (entity == null) {
            return null;
        }

        return UserDocument.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .email(entity.getEmail())
                .passwordHash(entity.getPasswordHash())
                .phoneNumber(entity.getPhoneNumber())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .emailVerified(entity.isEmailVerified())
                .verifiedAt(entity.getVerifiedAt())
                .avatarUrl(entity.getAvatarUrl())
                .birthday(entity.getBirthday())
                .birthdayLastUpdatedAt(entity.getBirthdayLastUpdatedAt())
                .birthdayUpdateCount(entity.getBirthdayUpdateCount() == null ? 0 : entity.getBirthdayUpdateCount())
                .points(entity.getPoints() == null ? 0L : entity.getPoints())
                .providerType(entity.getProviderType())

                // ROLE
                .roleIds(entity.getRoleIds())

                .build();
    }
}
