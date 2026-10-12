package x10.zenfit.mvp.service.infrastructure.order.adapter;

import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderItem;
import x10.zenfit.mvp.service.infrastructure.order.db.mongodb.OrderDocument;
import x10.zenfit.mvp.service.infrastructure.order.db.mongodb.OrderItemDocument;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderEntity toEntity(OrderDocument document) {
        if (document == null) {
            return null;
        }

        List<OrderItem> items = document.getItems() == null
                ? new ArrayList<>()
                : document.getItems().stream()
                .map(OrderMapper::toItemEntity)
                .collect(Collectors.toList());

        return OrderEntity.builder()
                .id(document.getId())
                .orderCode(document.getOrderCode())
                .payosOrderCode(document.getPayosOrderCode())
                .userId(document.getUserId())
                .guestId(document.getGuestId())
                .guestEmail(document.getGuestEmail())
                .guestUsername(document.getGuestUsername())
                .receiverName(document.getReceiverName())
                .receiverPhone(document.getReceiverPhone())
                .receiverAddress(document.getReceiverAddress())
                .items(items)
                .subtotal(document.getSubtotal())
                .shippingFee(document.getShippingFee())
                .discountAmount(document.getDiscountAmount())
                .totalAmount(document.getTotalAmount())
                .voucherId(document.getVoucherId())
                .voucherCode(document.getVoucherCode())
                .pointsEarned(document.getPointsEarned())
                .pointsAwarded(document.getPointsAwarded())
                .paymentMethod(document.getPaymentMethod())
                .paymentStatus(document.getPaymentStatus())
                .status(document.getStatus())
                .note(document.getNote())
                .cancelReason(document.getCancelReason())
                .cancelReasonType(document.getCancelReasonType())
                .cancelReasonDetail(document.getCancelReasonDetail())
                .trackingCode(document.getTrackingCode())
                .shippingProvider(document.getShippingProvider())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }

    public static OrderDocument toDocument(OrderEntity entity) {
        if (entity == null) {
            return null;
        }

        List<OrderItemDocument> items = entity.getItems() == null
                ? new ArrayList<>()
                : entity.getItems().stream()
                .map(OrderMapper::toItemDocument)
                .collect(Collectors.toList());

        return OrderDocument.builder()
                .id(entity.getId())
                .orderCode(entity.getOrderCode())
                .payosOrderCode(entity.getPayosOrderCode())
                .userId(entity.getUserId())
                .guestId(entity.getGuestId())
                .guestEmail(entity.getGuestEmail())
                .guestUsername(entity.getGuestUsername())
                .receiverName(entity.getReceiverName())
                .receiverPhone(entity.getReceiverPhone())
                .receiverAddress(entity.getReceiverAddress())
                .items(items)
                .subtotal(entity.getSubtotal())
                .shippingFee(entity.getShippingFee())
                .discountAmount(entity.getDiscountAmount())
                .totalAmount(entity.getTotalAmount())
                .voucherId(entity.getVoucherId())
                .voucherCode(entity.getVoucherCode())
                .pointsEarned(entity.getPointsEarned())
                .pointsAwarded(entity.getPointsAwarded())
                .paymentMethod(entity.getPaymentMethod())
                .paymentStatus(entity.getPaymentStatus())
                .status(entity.getStatus())
                .note(entity.getNote())
                .cancelReason(entity.getCancelReason())
                .cancelReasonType(entity.getCancelReasonType())
                .cancelReasonDetail(entity.getCancelReasonDetail())
                .trackingCode(entity.getTrackingCode())
                .shippingProvider(entity.getShippingProvider())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }

    private static OrderItem toItemEntity(OrderItemDocument document) {
        if (document == null) {
            return null;
        }

        return OrderItem.builder()
                .productId(document.getProductId())
                .productName(document.getProductName())
                .productImage(document.getProductImage())
                .colorId(document.getColorId())
                .colorName(document.getColorName())
                .sizeId(document.getSizeId())
                .sizeName(document.getSizeName())
                .quantity(document.getQuantity())
                .unitPrice(document.getUnitPrice())
                .totalPrice(document.getTotalPrice())
                .build();
    }

    private static OrderItemDocument toItemDocument(OrderItem entity) {
        if (entity == null) {
            return null;
        }

        return OrderItemDocument.builder()
                .productId(entity.getProductId())
                .productName(entity.getProductName())
                .productImage(entity.getProductImage())
                .colorId(entity.getColorId())
                .colorName(entity.getColorName())
                .sizeId(entity.getSizeId())
                .sizeName(entity.getSizeName())
                .quantity(entity.getQuantity())
                .unitPrice(entity.getUnitPrice())
                .totalPrice(entity.getTotalPrice())
                .build();
    }
}
