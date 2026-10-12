package x10.zenfit.mvp.service.service.refundService;

import x10.zenfit.mvp.service.common.domain.entities.order.RefundEntity;

public class RefundResponseMapper {

    public static RefundResponse toResponse(RefundEntity entity) {
        if (entity == null) {
            return null;
        }

        return RefundResponse.builder()
                .id(entity.getId())
                .orderId(entity.getOrderId())
                .paymentId(entity.getPaymentId())
                .userId(entity.getUserId())
                .type(entity.getType())
                .refundAmount(entity.getRefundAmount())
                .reason(entity.getReason())
                .imageUrls(entity.getImageUrls())
                .productId(entity.getProductId())
                .productName(entity.getProductName())
                .colorId(entity.getColorId())
                .colorName(entity.getColorName())
                .currentSizeId(entity.getCurrentSizeId())
                .currentSizeName(entity.getCurrentSizeName())
                .requestedSizeId(entity.getRequestedSizeId())
                .requestedSizeName(entity.getRequestedSizeName())
                .bankCode(entity.getBankCode())
                .bankName(entity.getBankName())
                .accountNumber(entity.getAccountNumber())
                .accountName(entity.getAccountName())
                .transferContent(entity.getTransferContent())
                .adminNote(entity.getAdminNote())
                .rejectedReason(entity.getRejectedReason())
                .provider(entity.getProvider())
                .refundTransactionId(entity.getRefundTransactionId())
                .providerResponseCode(entity.getProviderResponseCode())
                .providerResponseMessage(entity.getProviderResponseMessage())
                .status(entity.getStatus())
                .requestedAt(entity.getRequestedAt())
                .approvedAt(entity.getApprovedAt())
                .receivedAt(entity.getReceivedAt())
                .rejectedAt(entity.getRejectedAt())
                .processedAt(entity.getProcessedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
