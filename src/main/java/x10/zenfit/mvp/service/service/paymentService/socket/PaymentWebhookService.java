package x10.zenfit.mvp.service.service.paymentService.socket;

import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.model.webhooks.Webhook;
import vn.payos.model.webhooks.WebhookData;
import x10.zenfit.mvp.service.Repository.IOrderRepository;
import x10.zenfit.mvp.service.Repository.IPaymentRepository;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;
import x10.zenfit.mvp.service.common.domain.entities.order.PaymentEntity;
import x10.zenfit.mvp.service.common.domain.enums.PaymentStatus;
import x10.zenfit.mvp.service.service.orderService.OrderInventoryService;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentWebhookService {

    private final IOrderRepository orderRepository;
    private final IPaymentRepository paymentRepository;
    private final OrderInventoryService orderInventoryService;
    private final SimpMessagingTemplate messagingTemplate;
    private final PayOS payOS;

    public void handleWebhook(Webhook webhook) {
        WebhookData data = payOS.webhooks().verify(webhook);
        Long payosOrderCode = data.getOrderCode();

        OrderEntity order = orderRepository.findByPayosOrderCode(payosOrderCode)
                .orElseThrow(() -> new RuntimeException("Order not found by payOS orderCode"));

        PaymentStatus paymentStatus = "00".equals(data.getCode())
                ? PaymentStatus.PAID
                : PaymentStatus.FAILED;

        order.setPaymentStatus(paymentStatus);
        if (PaymentStatus.PAID == paymentStatus) {
            orderInventoryService.confirmOrder(order);
        }
        order.setUpdatedAt(LocalDateTime.now());

        orderRepository.save(order);

        LocalDateTime now = LocalDateTime.now();
        PaymentEntity payment = paymentRepository.findByProviderOrderCode(payosOrderCode)
                .orElseGet(() -> PaymentEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .orderId(order.getId())
                        .orderCode(order.getOrderCode())
                        .method(order.getPaymentMethod())
                        .amount(order.getTotalAmount())
                        .currency("VND")
                        .provider("PAYOS")
                        .providerOrderCode(payosOrderCode)
                        .createdAt(now)
                        .build());
        payment.setStatus(paymentStatus);
        payment.setFailureReason(PaymentStatus.FAILED == paymentStatus ? data.getDesc() : null);
        payment.setPaidAt(PaymentStatus.PAID == paymentStatus ? now : payment.getPaidAt());
        payment.setUpdatedAt(now);
        paymentRepository.save(payment);

        PaymentSocketMessage message = PaymentSocketMessage.builder()
                .orderId(order.getId())
                .orderCode(payosOrderCode)
                .paymentStatus(paymentStatus)
                .message(PaymentStatus.PAID == paymentStatus ? "Thanh toan thanh cong" : "Thanh toan that bai")
                .build();

        messagingTemplate.convertAndSend(
                "/topic/payment/" + order.getId(),
                message
        );
    }
}
