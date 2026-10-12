package x10.zenfit.mvp.service.infrastructure.refund.adapter;

import x10.zenfit.mvp.service.common.domain.entities.order.RefundEntity;
import x10.zenfit.mvp.service.infrastructure.refund.db.mongodb.RefundDocument;

public class RefundMapper {

    public static RefundEntity toEntity(RefundDocument document) {
        if (document == null) {
            return null;
        }

        return RefundEntity.builder()
                .id(document.getId())
                .orderId(document.getOrderId())
                .paymentId(document.getPaymentId())
                .userId(document.getUserId())
                .type(document.getType())
                .refundAmount(document.getRefundAmount())
                .reason(document.getReason())
                .imageUrls(document.getImageUrls())
                .productId(document.getProductId())
                .productName(document.getProductName())
                .colorId(document.getColorId())
                .colorName(document.getColorName())
                .currentSizeId(document.getCurrentSizeId())
                .currentSizeName(document.getCurrentSizeName())
                .requestedSizeId(document.getRequestedSizeId())
                .requestedSizeName(document.getRequestedSizeName())
                .bankCode(document.getBankCode())
                .bankName(document.getBankName())
                .accountNumber(document.getAccountNumber())
                .accountName(document.getAccountName())
                .transferContent(document.getTransferContent())
                .adminNote(document.getAdminNote())
                .rejectedReason(document.getRejectedReason())
                .provider(document.getProvider())
                .refundTransactionId(document.getRefundTransactionId())
                .providerResponseCode(document.getProviderResponseCode())
                .providerResponseMessage(document.getProviderResponseMessage())
                .status(document.getStatus())
                .requestedAt(document.getRequestedAt())
                .approvedAt(document.getApprovedAt())
                .receivedAt(document.getReceivedAt())
                .rejectedAt(document.getRejectedAt())
                .processedAt(document.getProcessedAt())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }

    public static RefundDocument toDocument(RefundEntity entity) {
        if (entity == null) {
            return null;
        }

        return RefundDocument.builder()
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
