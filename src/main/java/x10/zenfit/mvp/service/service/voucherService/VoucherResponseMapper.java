package x10.zenfit.mvp.service.service.voucherService;

import x10.zenfit.mvp.service.common.domain.entities.voucher.VoucherEntity;

public class VoucherResponseMapper {
    public static VoucherResponse toResponse(VoucherEntity voucher) {
        if (voucher == null) {
            return null;
        }

        return VoucherResponse.builder()
                .id(voucher.getId())
                .code(voucher.getCode())
                .name(voucher.getName())
                .description(voucher.getDescription())
                .discountType(voucher.getDiscountType())
                .discountValue(voucher.getDiscountValue())
                .minOrderAmount(voucher.getMinOrderAmount())
                .maxDiscountAmount(voucher.getMaxDiscountAmount())
                .usageLimit(voucher.getUsageLimit())
                .usedCount(voucher.getUsedCount())
                .assignedUserId(voucher.getAssignedUserId())
                .source(voucher.getSource())
                .eventName(voucher.getEventName())
                .active(voucher.isActive())
                .startAt(voucher.getStartAt())
                .endAt(voucher.getEndAt())
                .createdAt(voucher.getCreatedAt())
                .updatedAt(voucher.getUpdatedAt())
                .build();
    }
}
