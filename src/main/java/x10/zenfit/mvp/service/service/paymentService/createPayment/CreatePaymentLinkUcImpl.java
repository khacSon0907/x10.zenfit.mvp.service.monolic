package x10.zenfit.mvp.service.service.paymentService.createPayment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IOrderRepository;
import x10.zenfit.mvp.service.Repository.IPaymentRepository;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;
import x10.zenfit.mvp.service.common.domain.entities.order.PaymentEntity;
import x10.zenfit.mvp.service.common.domain.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreatePaymentLinkUcImpl implements ICreatePaymentLinkUc {

    private final IOrderRepository orderRepository;
    private final IPaymentRepository paymentRepository;
    private final PayOSService payOSService;

    @Override
    public CreatePaymentLinkResponse createPaymentLink(
            CreatePaymentLinkRequest request
    ) {

        OrderEntity order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

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
