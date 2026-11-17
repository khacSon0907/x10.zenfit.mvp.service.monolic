package x10.zenfit.mvp.service.infrastructure.subscription.adapter.mapper;

import x10.zenfit.mvp.service.common.domain.entities.subscription.SubscriptionPlanEntity;
import x10.zenfit.mvp.service.infrastructure.subscription.db.mongodb.SubscriptionPlanDocument;

public final class SubscriptionPlanMapper {

    private SubscriptionPlanMapper() {
    }

    public static SubscriptionPlanDocument toDocument(SubscriptionPlanEntity entity) {
        if (entity == null) {
            return null;
        }

        return SubscriptionPlanDocument.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .durationDays(entity.getDurationDays())
                .maxStudents(entity.getMaxStudents())
                .maxTrainingSessions(entity.getMaxTrainingSessions())
                .status(entity.getStatus())
                .isDefault(entity.getIsDefault())
                .displayOrder(entity.getDisplayOrder())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static SubscriptionPlanEntity toEntity(SubscriptionPlanDocument doc) {
        if (doc == null) {
            return null;
        }

        return SubscriptionPlanEntity.builder()
                .id(doc.getId())
                .code(doc.getCode())
                .name(doc.getName())
                .description(doc.getDescription())
                .price(doc.getPrice())
                .durationDays(doc.getDurationDays())
                .maxStudents(doc.getMaxStudents())
                .maxTrainingSessions(doc.getMaxTrainingSessions())
                .status(doc.getStatus())
                .isDefault(doc.getIsDefault())
                .displayOrder(doc.getDisplayOrder())
                .createdAt(doc.getCreatedAt())
                .updatedAt(doc.getUpdatedAt())
                .build();
    }
}

