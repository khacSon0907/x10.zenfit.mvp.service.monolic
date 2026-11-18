package x10.zenfit.mvp.service.infrastructure.subscription.adapter.mapper;

import x10.zenfit.mvp.service.common.domain.entities.subscription.UserSubscriptionEntity;
import x10.zenfit.mvp.service.infrastructure.subscription.db.mongodb.UserSubscriptionDocument;

public final class UserSubscriptionMapper {

    private UserSubscriptionMapper() {
    }

    public static UserSubscriptionDocument toDocument(UserSubscriptionEntity entity) {
        if (entity == null) {
            return null;
        }

        return UserSubscriptionDocument.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .planId(entity.getPlanId())
                .status(entity.getStatus())
                .startAt(entity.getStartAt())
                .endAt(entity.getEndAt())
                .price(entity.getPrice())
                .maxStudents(entity.getMaxStudents())
                .currentStudents(entity.getCurrentStudents())
                .autoRenew(entity.getAutoRenew())
                .paidAt(entity.getPaidAt())
                .transactionId(entity.getTransactionId())
                .payosOrderCode(entity.getPayosOrderCode())
                .checkoutUrl(entity.getCheckoutUrl())
                .qrCode(entity.getQrCode())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    public static UserSubscriptionEntity toEntity(UserSubscriptionDocument doc) {
        if (doc == null) {
            return null;
        }

        return UserSubscriptionEntity.builder()
                .id(doc.getId())
                .userId(doc.getUserId())
                .planId(doc.getPlanId())
                .status(doc.getStatus())
                .startAt(doc.getStartAt())
                .endAt(doc.getEndAt())
                .price(doc.getPrice())
                .maxStudents(doc.getMaxStudents())
                .currentStudents(doc.getCurrentStudents())
                .autoRenew(doc.getAutoRenew())
                .paidAt(doc.getPaidAt())
                .transactionId(doc.getTransactionId())
                .payosOrderCode(doc.getPayosOrderCode())
                .checkoutUrl(doc.getCheckoutUrl())
                .qrCode(doc.getQrCode())
                .createdAt(doc.getCreatedAt())
                .updatedAt(doc.getUpdatedAt())
                .build();
    }
}

