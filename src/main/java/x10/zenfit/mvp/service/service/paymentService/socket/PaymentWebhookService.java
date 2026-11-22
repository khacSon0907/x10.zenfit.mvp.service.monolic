package x10.zenfit.mvp.service.service.paymentService.socket;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.model.webhooks.Webhook;
import vn.payos.model.webhooks.WebhookData;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.Repository.IOrderRepository;
import x10.zenfit.mvp.service.Repository.IPaymentRepository;
import x10.zenfit.mvp.service.Repository.IUserRepository;
import x10.zenfit.mvp.service.Repository.IUserSubscriptionRepository;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;
import x10.zenfit.mvp.service.common.domain.entities.order.PaymentEntity;
import x10.zenfit.mvp.service.common.domain.entities.subscription.UserSubscriptionEntity;
import x10.zenfit.mvp.service.common.domain.enums.PaymentStatus;
import x10.zenfit.mvp.service.common.domain.enums.UserSubscriptionStatus;
import x10.zenfit.mvp.service.service.orderService.OrderInventoryService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentWebhookService {

    private final IOrderRepository orderRepository;
    private final IPaymentRepository paymentRepository;
    private final IUserSubscriptionRepository userSubscriptionRepository;
    private final IUserRepository userRepository;
    private final IClientRepository clientRepository;
    private final OrderInventoryService orderInventoryService;
    private final SimpMessagingTemplate messagingTemplate;
    private final PayOS payOS;

    public void handleWebhook(Webhook webhook) {
        WebhookData data = payOS.webhooks().verify(webhook);
        Long payosOrderCode = data.getOrderCode();

        log.info("Processing PayOS webhook for orderCode: {}, code: {}, amount: {}",
                payosOrderCode, data.getCode(), data.getAmount());

        // 1. Kiểm tra xem webhook này thuộc về UserSubscription hay Order bán hàng
        Optional<UserSubscriptionEntity> subOpt = userSubscriptionRepository.findByPayosOrderCode(payosOrderCode);
        if (subOpt.isPresent()) {
            handleSubscriptionWebhook(subOpt.get(), data);
            return;
        }

        // 2. Nếu không phải Subscription, xử lý theo luồng Order bán hàng
        handleOrderWebhook(payosOrderCode, data);
    }

    private void handleSubscriptionWebhook(UserSubscriptionEntity subscription, WebhookData data) {
        Long payosOrderCode = data.getOrderCode();
        String userId = subscription.getUserId();

        boolean isSuccessCode = "00".equals(data.getCode());

        if (!isSuccessCode) {
            log.warn("PayOS payment failed for subscription: {}, code: {}, desc: {}",
                    subscription.getId(), data.getCode(), data.getDesc());

            subscription.setStatus(UserSubscriptionStatus.CANCELLED);
            subscription.setUpdatedAt(Instant.now());
            userSubscriptionRepository.save(subscription);

            PaymentSocketMessage message = PaymentSocketMessage.builder()
                    .orderId(subscription.getId())
                    .orderCode(payosOrderCode)
                    .subscriptionId(subscription.getId())
                    .planId(subscription.getPlanId())
                    .userId(userId)
                    .success(false)
                    .paymentStatus(PaymentStatus.FAILED)
                    .amount(data.getAmount())
                    .message("Thanh toán gói dịch vụ thất bại hoặc đã bị hủy: " + (data.getDesc() != null ? data.getDesc() : ""))
                    .build();

            messagingTemplate.convertAndSend("/topic/subscription-payment/" + userId, message);
            messagingTemplate.convertAndSend("/topic/payment/" + subscription.getId(), message);
            return;
        }

        // Kiểm tra số tiền: nếu không đủ thì báo lỗi
        long expectedAmount = subscription.getPrice() != null ? subscription.getPrice().longValue() : 0L;
        long actualAmount = data.getAmount() != null ? data.getAmount() : 0L;

        if (actualAmount < expectedAmount) {
            log.error("PayOS subscription amount mismatch! Expected: {}, Actual: {}", expectedAmount, actualAmount);

            PaymentSocketMessage message = PaymentSocketMessage.builder()
                    .orderId(subscription.getId())
                    .orderCode(payosOrderCode)
                    .subscriptionId(subscription.getId())
                    .planId(subscription.getPlanId())
                    .userId(userId)
                    .success(false)
                    .paymentStatus(PaymentStatus.FAILED)
                    .amount(actualAmount)
                    .message("Số tiền thanh toán (" + actualAmount + " VND) không đủ so với giá gói (" + expectedAmount + " VND). Vui lòng liên hệ hỗ trợ.")
                    .build();

            messagingTemplate.convertAndSend("/topic/subscription-payment/" + userId, message);
            messagingTemplate.convertAndSend("/topic/payment/" + subscription.getId(), message);
            return;
        }

        // Thanh toán thành công và đủ số tiền
        Instant now = Instant.now();

        // Chuyển gói cũ sang EXPIRED (nếu có gói active khác của user này)
        userSubscriptionRepository.findActiveSubscriptionByUserId(userId).ifPresent(oldSub -> {
            if (!oldSub.getId().equals(subscription.getId())) {
                oldSub.setStatus(UserSubscriptionStatus.EXPIRED);
                oldSub.setUpdatedAt(now);
                userSubscriptionRepository.save(oldSub);
                log.info("Old subscription {} expired after upgrade to new subscription {}", oldSub.getId(), subscription.getId());
            }
        });

        // Đồng bộ số học viên thực tế hiện tại
        long realStudents = clientRepository.countByAssignedTrainerId(userId);

        subscription.setStatus(UserSubscriptionStatus.ACTIVE);
        subscription.setCurrentStudents((int) realStudents);
        subscription.setPaidAt(now);
        subscription.setTransactionId(data.getReference() != null ? data.getReference() : String.valueOf(payosOrderCode));
        subscription.setUpdatedAt(now);
        userSubscriptionRepository.save(subscription);

        // Kích hoạt subscriptionConfirmed = true cho User
        userRepository.findById(userId).ifPresent(user -> {
            user.setSubscriptionConfirmed(true);
            user.setUpdatedAt(now);
            userRepository.save(user);
            log.info("User {} subscriptionConfirmed set to true successfully.", userId);
        });

        PaymentSocketMessage message = PaymentSocketMessage.builder()
                .orderId(subscription.getId())
                .orderCode(payosOrderCode)
                .subscriptionId(subscription.getId())
                .planId(subscription.getPlanId())
                .userId(userId)
                .success(true)
                .paymentStatus(PaymentStatus.PAID)
                .amount(actualAmount)
                .message("Thanh toán gói dịch vụ thành công! Chào mừng bạn gia nhập ZenFit.")
                .build();

        messagingTemplate.convertAndSend("/topic/subscription-payment/" + userId, message);
        messagingTemplate.convertAndSend("/topic/payment/" + subscription.getId(), message);

        log.info("WebSocket payment success notification sent for user: {}", userId);
    }

    private void handleOrderWebhook(Long payosOrderCode, WebhookData data) {
        OrderEntity order = orderRepository.findByPayosOrderCode(payosOrderCode)
                .orElseThrow(() -> new RuntimeException("Order not found by payOS orderCode: " + payosOrderCode));

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
                .success(PaymentStatus.PAID == paymentStatus)
                .paymentStatus(paymentStatus)
                .amount(data.getAmount())
                .message(PaymentStatus.PAID == paymentStatus ? "Thanh toán đơn hàng thành công" : "Thanh toán đơn hàng thất bại")
                .build();

        messagingTemplate.convertAndSend(
                "/topic/payment/" + order.getId(),
                message
        );
    }
}
