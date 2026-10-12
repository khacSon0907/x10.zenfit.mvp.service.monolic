package x10.zenfit.mvp.service.infrastructure.payment.adapter;

import x10.zenfit.mvp.service.common.domain.entities.order.PaymentEntity;
import x10.zenfit.mvp.service.infrastructure.payment.db.mongodb.PaymentDocument;

public class PaymentMapper {

    public static PaymentEntity toEntity(PaymentDocument document) {
        if (document == null) {
            return null;
        }

        return PaymentEntity.builder()
                .id(document.getId())
                .orderId(document.getOrderId())
                .orderCode(document.getOrderCode())
                .method(document.getMethod())
                .status(document.getStatus())
                .amount(document.getAmount())
                .currency(document.getCurrency())
                .provider(document.getProvider())
                .providerOrderCode(document.getProviderOrderCode())
                .providerPaymentLinkId(document.getProviderPaymentLinkId())
                .checkoutUrl(document.getCheckoutUrl())
                .qrCode(document.getQrCode())
                .providerTransactionId(document.getProviderTransactionId())
                .failureReason(document.getFailureReason())
                .paidAt(document.getPaidAt())
                .refundedAt(document.getRefundedAt())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }

    public static PaymentDocument toDocument(PaymentEntity entity) {
        if (entity == null) {
            return null;
        }

        return PaymentDocument.builder()
                .id(entity.getId())
                .orderId(entity.getOrderId())
                .orderCode(entity.getOrderCode())
                .method(entity.getMethod())
                .status(entity.getStatus())
                .amount(entity.getAmount())
                .currency(entity.getCurrency())
                .provider(entity.getProvider())
                .providerOrderCode(entity.getProviderOrderCode())
                .providerPaymentLinkId(entity.getProviderPaymentLinkId())
                .checkoutUrl(entity.getCheckoutUrl())
                .qrCode(entity.getQrCode())
                .providerTransactionId(entity.getProviderTransactionId())
                .failureReason(entity.getFailureReason())
                .paidAt(entity.getPaidAt())
                .refundedAt(entity.getRefundedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
