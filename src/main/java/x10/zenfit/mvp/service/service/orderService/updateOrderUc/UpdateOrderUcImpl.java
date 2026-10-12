package x10.zenfit.mvp.service.service.orderService.updateOrderUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IOrderRepository;
import x10.zenfit.mvp.service.Repository.IProductRepository;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderItem;
import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;
import x10.zenfit.mvp.service.common.domain.enums.CancelReasonType;
import x10.zenfit.mvp.service.common.domain.enums.OrderStatus;
import x10.zenfit.mvp.service.service.orderService.OrderInventoryService;
import x10.zenfit.mvp.service.service.orderService.OrderResponse;
import x10.zenfit.mvp.service.service.orderService.OrderResponseMapper;
import x10.zenfit.mvp.service.service.pointService.ICorePointService;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.order.OrderError;
import x10.zenfit.mvp.service.share.exception.product.ProductError;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UpdateOrderUcImpl implements IUpdateOrderUc {

    private static final Map<OrderStatus, List<OrderStatus>> ALLOWED_STATUS_TRANSITIONS = new EnumMap<>(OrderStatus.class);

    static {
        ALLOWED_STATUS_TRANSITIONS.put(OrderStatus.CONFIRMED, List.of(OrderStatus.PACKING));
        ALLOWED_STATUS_TRANSITIONS.put(OrderStatus.PACKING, List.of(OrderStatus.SHIPPING));
        ALLOWED_STATUS_TRANSITIONS.put(OrderStatus.SHIPPING, List.of(OrderStatus.COMPLETED));
        ALLOWED_STATUS_TRANSITIONS.put(OrderStatus.COMPLETED, List.of(OrderStatus.RETURNED, OrderStatus.EXCHANGED, OrderStatus.REFUNDED));
        ALLOWED_STATUS_TRANSITIONS.put(OrderStatus.RETURNED, List.of(OrderStatus.REFUNDED));
    }

    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;
    private final OrderInventoryService orderInventoryService;
    private final ICorePointService pointService;

    @Override
    public OrderResponse execute(String orderId, UpdateOrderRequest request) {
        OrderEntity order = orderRepository.findById(orderId)
                .or(() -> orderRepository.findByOrderCode(orderId))
                .orElseThrow(() -> new BusinessException(OrderError.ORDER_NOT_FOUND));

        if (request.getReceiverName() != null && !request.getReceiverName().isBlank()) {
            order.setReceiverName(request.getReceiverName());
        }
        if (request.getReceiverPhone() != null && !request.getReceiverPhone().isBlank()) {
            order.setReceiverPhone(request.getReceiverPhone());
        }
        if (request.getReceiverAddress() != null && !request.getReceiverAddress().isBlank()) {
            order.setReceiverAddress(request.getReceiverAddress());
        }
        if (request.getPaymentMethod() != null) {
            order.setPaymentMethod(request.getPaymentMethod());
        }
        if (request.getPaymentStatus() != null) {
            order.setPaymentStatus(request.getPaymentStatus());
        }
        if (request.getNote() != null) {
            order.setNote(request.getNote());
        }
        if (request.getCancelReason() != null) {
            order.setCancelReason(request.getCancelReason());
        }
        if (request.getCancelReasonType() != null) {
            order.setCancelReasonType(request.getCancelReasonType());
        }
        if (request.getCancelReasonDetail() != null) {
            order.setCancelReasonDetail(request.getCancelReasonDetail());
        }
        if (request.getTrackingCode() != null) {
            order.setTrackingCode(request.getTrackingCode());
        }
        if (request.getShippingProvider() != null) {
            order.setShippingProvider(request.getShippingProvider());
        }
        if (request.getShippingFee() != null) {
            order.setShippingFee(request.getShippingFee());
        }
        if (request.getDiscountAmount() != null) {
            order.setDiscountAmount(request.getDiscountAmount());
        }
        if (request.getItems() != null) {
            if (order.getStatus() != OrderStatus.PENDING) {
                throw new BusinessException(
                        OrderError.INVALID_ORDER_STATUS,
                        "Chi co the cap nhat san pham khi don hang dang cho xu ly"
                );
            }
            if (request.getItems().isEmpty()) {
                throw new BusinessException(OrderError.INVALID_ORDER_DATA);
            }
            order.setItems(request.getItems().stream()
                    .map(this::buildOrderItem)
                    .collect(Collectors.toList()));
        }

        recalculateOrder(order);

        if (request.getStatus() != null) {
            applyStatusChange(order, request.getStatus(), request.getCancelReasonType(), request.getCancelReasonDetail());
        }

        order.setUpdatedAt(LocalDateTime.now());

        return OrderResponseMapper.toResponse(orderRepository.save(order));
    }

    private void applyStatusChange(
            OrderEntity order,
            OrderStatus requestedStatus,
            CancelReasonType cancelReasonType,
            String cancelReasonDetail
    ) {
        if (requestedStatus == order.getStatus()) {
            return;
        }

        if (requestedStatus == OrderStatus.CONFIRMED) {
            orderInventoryService.confirmOrder(order);
            return;
        }

        if (requestedStatus == OrderStatus.CANCELLED) {
            validateCancelReason(cancelReasonType, cancelReasonDetail);
            orderInventoryService.cancelOrder(
                    order,
                    cancelReasonType,
                    buildCancelReason(cancelReasonType, cancelReasonDetail),
                    normalizeText(cancelReasonDetail)
            );
            return;
        }

        if (order.getStatus() == OrderStatus.PENDING) {
            throw new BusinessException(
                    OrderError.INVALID_ORDER_STATUS,
                    "Don hang dang cho xu ly chi co the xac nhan hoac huy"
            );
        }

        OrderStatus currentStatus = normalizeCurrentStatus(order.getStatus());
        OrderStatus nextStatus = normalizeRequestedStatus(requestedStatus);
        if (canMoveTo(currentStatus, nextStatus)) {
            order.setStatus(nextStatus);
            if (nextStatus == OrderStatus.COMPLETED) {
                pointService.awardPointsForCompletedOrder(order);
            }
            return;
        }

        throw new BusinessException(
                OrderError.INVALID_ORDER_STATUS,
                "Trang thai don hang khong hop le theo luong: CHUA_XAC_NHAN -> XAC_NHAN -> DONG_GOI -> GIAO_HANG -> THANH_CONG -> TRA_HANG/DOI_HANG/HOAN_TIEN_THANH_CONG"
        );
    }

    private void validateCancelReason(CancelReasonType cancelReasonType, String cancelReasonDetail) {
        if (cancelReasonType == null) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA, "Vui long chon ly do huy don");
        }

        if (cancelReasonType == CancelReasonType.OTHER && normalizeText(cancelReasonDetail) == null) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA, "Vui long nhap ly do huy don khac");
        }
    }

    private String buildCancelReason(CancelReasonType cancelReasonType, String cancelReasonDetail) {
        String detail = normalizeText(cancelReasonDetail);
        if (detail == null) {
            return cancelReasonType.getLabel();
        }
        return cancelReasonType.getLabel() + ": " + detail;
    }

    private String normalizeText(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private boolean canMoveTo(OrderStatus currentStatus, OrderStatus nextStatus) {
        return ALLOWED_STATUS_TRANSITIONS
                .getOrDefault(currentStatus, List.of())
                .contains(nextStatus);
    }

    private OrderStatus normalizeCurrentStatus(OrderStatus status) {
        if (status == OrderStatus.DELIVERED) {
            return OrderStatus.COMPLETED;
        }
        if (status == OrderStatus.RETURN_REQUESTED
                || status == OrderStatus.RETURN_APPROVED
                || status == OrderStatus.RETURN_RECEIVED) {
            return OrderStatus.RETURNED;
        }
        if (status == OrderStatus.EXCHANGE_APPROVED) {
            return OrderStatus.EXCHANGED;
        }
        if (status == OrderStatus.REFUND_PROCESSING || status == OrderStatus.REFUND_SUCCESS) {
            return OrderStatus.REFUNDED;
        }
        return status;
    }

    private OrderStatus normalizeRequestedStatus(OrderStatus status) {
        return normalizeCurrentStatus(status);
    }

    private OrderItem buildOrderItem(UpdateOrderRequest.OrderItemRequest request) {
        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new BusinessException(ProductError.PRODUCT_NOT_FOUND));

        BigDecimal unitPrice = product.getSalePrice() != null ? product.getSalePrice() : product.getPrice();
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA, "Gia san pham khong hop le");
        }

        return OrderItem.builder()
                .productId(product.getId())
                .productName(product.getName())
                .productImage(request.getProductImage())
                .colorId(request.getColorId())
                .colorName(request.getColorName())
                .sizeId(request.getSizeId())
                .sizeName(request.getSizeName())
                .quantity(request.getQuantity())
                .unitPrice(unitPrice)
                .totalPrice(unitPrice.multiply(BigDecimal.valueOf(request.getQuantity())))
                .build();
    }

    private void recalculateOrder(OrderEntity order) {
        List<OrderItem> items = order.getItems();
        if (items == null || items.isEmpty()) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA);
        }

        BigDecimal subtotal = items.stream()
                .map(item -> {
                    BigDecimal totalPrice = item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity()));
                    item.setTotalPrice(totalPrice);
                    return totalPrice;
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal shippingFee = order.getShippingFee() == null ? BigDecimal.ZERO : order.getShippingFee();
        BigDecimal discountAmount = order.getDiscountAmount() == null ? BigDecimal.ZERO : order.getDiscountAmount();
        BigDecimal totalAmount = subtotal.add(shippingFee).subtract(discountAmount);

        if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA, "Tong tien don hang khong hop le");
        }

        order.setSubtotal(subtotal);
        order.setShippingFee(shippingFee);
        order.setDiscountAmount(discountAmount);
        order.setTotalAmount(totalAmount);
    }

}
