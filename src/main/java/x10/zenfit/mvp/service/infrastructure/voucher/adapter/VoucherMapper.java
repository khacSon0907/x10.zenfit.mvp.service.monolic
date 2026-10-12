package x10.zenfit.mvp.service.infrastructure.voucher.adapter;

import x10.zenfit.mvp.service.common.domain.entities.voucher.VoucherEntity;
import x10.zenfit.mvp.service.infrastructure.voucher.db.mongodb.VoucherDocument;

public class VoucherMapper {
    public static VoucherEntity toEntity(VoucherDocument document) {
        if (document == null) {
            return null;
        }

        return VoucherEntity.builder()
                .id(document.getId())
                .code(document.getCode())
                .name(document.getName())
                .description(document.getDescription())
                .discountType(document.getDiscountType())
                .discountValue(document.getDiscountValue())
                .minOrderAmount(document.getMinOrderAmount())
                .maxDiscountAmount(document.getMaxDiscountAmount())
                .usageLimit(document.getUsageLimit())
                .usedCount(document.getUsedCount())
                .assignedUserId(document.getAssignedUserId())
                .source(document.getSource())
                .eventName(document.getEventName())
                .active(document.isActive())
                .startAt(document.getStartAt())
                .endAt(document.getEndAt())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }

    public static VoucherDocument toDocument(VoucherEntity entity) {
        if (entity == null) {
            return null;
        }

        return VoucherDocument.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .discountType(entity.getDiscountType())
                .discountValue(entity.getDiscountValue())
                .minOrderAmount(entity.getMinOrderAmount())
                .maxDiscountAmount(entity.getMaxDiscountAmount())
                .usageLimit(entity.getUsageLimit())
                .usedCount(entity.getUsedCount())
                .assignedUserId(entity.getAssignedUserId())
                .source(entity.getSource())
                .eventName(entity.getEventName())
                .active(entity.isActive())
                .startAt(entity.getStartAt())
                .endAt(entity.getEndAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
