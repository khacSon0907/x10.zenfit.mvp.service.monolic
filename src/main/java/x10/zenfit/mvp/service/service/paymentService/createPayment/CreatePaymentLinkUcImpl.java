package x10.zenfit.mvp.service.service.paymentService.createPayment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IOrderRepository;
import x10.zenfit.mvp.service.Repository.IPaymentRepository;
import x10.zenfit.mvp.service.Repository.ISubscriptionPlanRepository;
import x10.zenfit.mvp.service.Repository.IUserSubscriptionRepository;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;
import x10.zenfit.mvp.service.common.domain.entities.order.PaymentEntity;
import x10.zenfit.mvp.service.common.domain.entities.subscription.SubscriptionPlanEntity;
import x10.zenfit.mvp.service.common.domain.entities.subscription.UserSubscriptionEntity;
import x10.zenfit.mvp.service.common.domain.enums.PaymentStatus;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreatePaymentLinkUcImpl implements ICreatePaymentLinkUc {

    private final IOrderRepository orderRepository;
    private final IPaymentRepository paymentRepository;
    private final IUserSubscriptionRepository userSubscriptionRepository;
    private final ISubscriptionPlanRepository subscriptionPlanRepository;
    private final PayOSService payOSService;

    @Override
    public CreatePaymentLinkResponse createPaymentLink(
            CreatePaymentLinkRequest request
    ) {
        if (request == null) {
            throw new IllegalArgumentException("Request cannot be null");
        }

        // 1. Nếu request thanh toán cho UserSubscription
        if (request.getSubscriptionId() != null && !request.getSubscriptionId().isBlank()) {
            UserSubscriptionEntity subscription = userSubscriptionRepository.findById(request.getSubscriptionId().trim())
                    .orElseThrow(() -> new RuntimeException("Subscription not found: " + request.getSubscriptionId()));

            SubscriptionPlanEntity plan = subscriptionPlanRepository.findById(subscription.getPlanId())
                    .orElseThrow(() -> new RuntimeException("Subscription plan not found: " + subscription.getPlanId()));

            Long orderCode = System.currentTimeMillis();
            String desc = ("DK " + plan.getCode()).replaceAll("[^a-zA-Z0-9 ]", "");
            if (desc.length() > 25) {
                desc = desc.substring(0, 25);
            }

            vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse payOSResponse =
                    payOSService.createPaymentLink(
                            orderCode,
                            plan.getPrice().longValue(),
                            desc
                    );

            subscription.setPayosOrderCode(orderCode);
            subscription.setCheckoutUrl(payOSResponse.getCheckoutUrl());
            subscription.setQrCode(payOSResponse.getQrCode());
            subscription.setUpdatedAt(Instant.now());
            userSubscriptionRepository.save(subscription);

            return CreatePaymentLinkResponse.builder()
                    .orderId(subscription.getId())
                    .orderCode(orderCode)
                    .paymentLinkId(payOSResponse.getPaymentLinkId())
                    .amount(plan.getPrice())
                    .checkoutUrl(payOSResponse.getCheckoutUrl())
                    .qrCode(payOSResponse.getQrCode())
                    .build();
        }

        // 2. Nếu request thanh toán cho Order bán hàng
        OrderEntity order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found: " + request.getOrderId()));

        Long orderCode = System.currentTimeMillis();

        vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse payOSResponse =
                payOSService.createPaymentLink(
                        orderCode,
                        order.getTotalAmount().longValue(),
                        "DH" + orderCode
                );

        order.setPayosOrderCode(orderCode);
        order.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(order);

        LocalDateTime now = LocalDateTime.now();
        paymentRepository.findByOrderId(order.getId())
                .orElseGet(() -> paymentRepository.save(PaymentEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .orderId(order.getId())
                        .orderCode(order.getOrderCode())
                        .method(order.getPaymentMethod())
                        .status(PaymentStatus.PENDING)
                        .amount(order.getTotalAmount())
                        .currency("VND")
                        .provider("PAYOS")
                        .providerOrderCode(orderCode)
                        .providerPaymentLinkId(payOSResponse.getPaymentLinkId())
                        .checkoutUrl(payOSResponse.getCheckoutUrl())
                        .qrCode(payOSResponse.getQrCode())
                        .createdAt(now)
                        .updatedAt(now)
                        .build()));

        return CreatePaymentLinkResponse.builder()
                .orderId(order.getId())
                .orderCode(orderCode)
                .paymentLinkId(payOSResponse.getPaymentLinkId())
                .amount(order.getTotalAmount())
                .checkoutUrl(payOSResponse.getCheckoutUrl())
                .qrCode(payOSResponse.getQrCode())
                .build();
    }
}
