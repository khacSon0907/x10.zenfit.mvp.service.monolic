package x10.zenfit.mvp.service.infrastructure.user.adapter.mapper;

import x10.zenfit.mvp.service.common.domain.entities.user.UserBirthdayHistoryEntity;
import x10.zenfit.mvp.service.infrastructure.user.db.mongodb.UserBirthdayHistoryDocument;

public class UserBirthdayHistoryMapper {
    public static UserBirthdayHistoryEntity toEntity(UserBirthdayHistoryDocument document) {
        if (document == null) {
            return null;
        }

        return UserBirthdayHistoryEntity.builder()
                .id(document.getId())
                .userId(document.getUserId())
                .oldBirthday(document.getOldBirthday())
                .newBirthday(document.getNewBirthday())
                .changedByUserId(document.getChangedByUserId())
                .changedByAdmin(document.isChangedByAdmin())
                .changedAt(document.getChangedAt())
                .build();
    }

    public static UserBirthdayHistoryDocument toDocument(UserBirthdayHistoryEntity entity) {
        if (entity == null) {
            return null;
        }

        return UserBirthdayHistoryDocument.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .oldBirthday(entity.getOldBirthday())
                .newBirthday(entity.getNewBirthday())
                .changedByUserId(entity.getChangedByUserId())
                .changedByAdmin(entity.isChangedByAdmin())
                .changedAt(entity.getChangedAt())
                .build();
    }
}
