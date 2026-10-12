package x10.zenfit.mvp.service.service.orderService;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;

@Service
@RequiredArgsConstructor
public class AdminOrderSocketService {

    public static final String ADMIN_ORDER_TOPIC = "/topic/admin/orders";

    private final SimpMessagingTemplate messagingTemplate;

    public void sendNewOrder(OrderEntity order) {
        if (order == null) {
            return;
        }

        AdminOrderSocketMessage message = AdminOrderSocketMessage.builder()
                .type("NEW_ORDER")
                .orderId(order.getId())
                .orderCode(order.getOrderCode())
                .userId(order.getUserId())
                .guestId(order.getGuestId())
                .guestEmail(order.getGuestEmail())
                .receiverName(order.getReceiverName())
                .receiverPhone(order.getReceiverPhone())
                .totalAmount(order.getTotalAmount())
                .paymentMethod(order.getPaymentMethod())
                .paymentStatus(order.getPaymentStatus())
                .status(order.getStatus())
                .itemCount(order.getItems() == null ? 0 : order.getItems().size())
                .createdAt(order.getCreatedAt())
                .message("Co don hang moi")
                .build();

        messagingTemplate.convertAndSend(ADMIN_ORDER_TOPIC, message);
    }
}
