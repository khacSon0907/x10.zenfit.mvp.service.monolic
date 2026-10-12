package x10.zenfit.mvp.service.service.orderService.createOrderUc;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.context.ApplicationEventPublisher;
import x10.zenfit.mvp.service.Repository.IGuestRepository;
import x10.zenfit.mvp.service.Repository.IOrderRepository;
import x10.zenfit.mvp.service.Repository.IProductRepository;
import x10.zenfit.mvp.service.Repository.IUserRepository;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;
import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;
import x10.zenfit.mvp.service.common.domain.entities.user.UserEntity;
import x10.zenfit.mvp.service.common.domain.entities.voucher.VoucherEntity;
import x10.zenfit.mvp.service.common.domain.enums.PaymentMethod;
import x10.zenfit.mvp.service.service.notification.event.OrderInvoiceEmailEvent;
import x10.zenfit.mvp.service.service.orderService.AdminOrderSocketService;
import x10.zenfit.mvp.service.service.orderService.ICoreShippingRuleService;
import x10.zenfit.mvp.service.service.paymentService.ICorePaymentService;
import x10.zenfit.mvp.service.service.voucherService.ICoreVoucherService;
import x10.zenfit.mvp.service.service.voucherService.VoucherCalculationResult;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CreateOrderUcImplTest {

    @Test
    void createOrderPublishesInvoiceEmailForRegisteredUser() {
        IOrderRepository orderRepository = mock(IOrderRepository.class);
        IProductRepository productRepository = mock(IProductRepository.class);
        IGuestRepository guestRepository = mock(IGuestRepository.class);
        IUserRepository userRepository = mock(IUserRepository.class);
        ICorePaymentService paymentService = mock(ICorePaymentService.class);
        ICoreShippingRuleService shippingRuleService = mock(ICoreShippingRuleService.class);
        ICoreVoucherService voucherService = mock(ICoreVoucherService.class);
        ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
        AdminOrderSocketService adminOrderSocketService = mock(AdminOrderSocketService.class);
        CreateOrderUcImpl useCase = new CreateOrderUcImpl(
                orderRepository,
                productRepository,
                guestRepository,
                userRepository,
                paymentService,
                shippingRuleService,
                voucherService,
                eventPublisher,
                adminOrderSocketService
        );

        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId("user-1");
        request.setReceiverName("Nguyen Van A");
        request.setReceiverPhone("0901234567");
        request.setReceiverAddress("Quan 1");
        request.setPaymentMethod(PaymentMethod.COD);
        CreateOrderRequest.OrderItemRequest item = new CreateOrderRequest.OrderItemRequest();
        item.setProductId("product-1");
        item.setColorId("color-1");
        item.setSizeId("size-1");
        item.setQuantity(1);
        request.setItems(List.of(item));

        ProductEntity product = ProductEntity.builder()
                .id("product-1")
                .name("Ao thun")
                .price(BigDecimal.valueOf(100_000))
                .build();
        UserEntity user = UserEntity.builder()
                .id("user-1")
                .username("Van A")
                .email("vana@example.com")
                .build();

        when(productRepository.findById("product-1")).thenReturn(Optional.of(product));
        when(shippingRuleService.calculateShippingFee(any(), any())).thenReturn(BigDecimal.ZERO);
        when(userRepository.findById("user-1")).thenReturn(Optional.of(user));
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        useCase.execute(request);

        ArgumentCaptor<Object> eventCaptor = ArgumentCaptor.forClass(Object.class);
        verify(eventPublisher).publishEvent(eventCaptor.capture());
        assertThat(eventCaptor.getValue()).isInstanceOf(OrderInvoiceEmailEvent.class);
        OrderInvoiceEmailEvent invoiceEvent = (OrderInvoiceEmailEvent) eventCaptor.getValue();
        assertThat(invoiceEvent.email()).isEqualTo("vana@example.com");
        assertThat(invoiceEvent.username()).isEqualTo("Van A");
        assertThat(invoiceEvent.order().getUserId()).isEqualTo("user-1");
    }

    @Test
    void createOrderMarksVoucherUsedImmediatelyAfterOrderIsSaved() {
        IOrderRepository orderRepository = mock(IOrderRepository.class);
        IProductRepository productRepository = mock(IProductRepository.class);
        IGuestRepository guestRepository = mock(IGuestRepository.class);
        IUserRepository userRepository = mock(IUserRepository.class);
        ICorePaymentService paymentService = mock(ICorePaymentService.class);
        ICoreShippingRuleService shippingRuleService = mock(ICoreShippingRuleService.class);
        ICoreVoucherService voucherService = mock(ICoreVoucherService.class);
        ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
        AdminOrderSocketService adminOrderSocketService = mock(AdminOrderSocketService.class);
        CreateOrderUcImpl useCase = new CreateOrderUcImpl(
                orderRepository,
                productRepository,
                guestRepository,
                userRepository,
                paymentService,
                shippingRuleService,
                voucherService,
                eventPublisher,
                adminOrderSocketService
        );

        CreateOrderRequest request = new CreateOrderRequest();
        request.setUserId("user-1");
        request.setReceiverName("Nguyen Van A");
        request.setReceiverPhone("0901234567");
        request.setReceiverAddress("Quan 1");
        request.setPaymentMethod(PaymentMethod.COD);
        request.setVoucherCode("SALE10");
        CreateOrderRequest.OrderItemRequest item = new CreateOrderRequest.OrderItemRequest();
        item.setProductId("product-1");
        item.setColorId("color-1");
        item.setSizeId("size-1");
        item.setQuantity(1);
        request.setItems(List.of(item));

        ProductEntity product = ProductEntity.builder()
                .id("product-1")
                .name("Ao thun")
                .price(BigDecimal.valueOf(100_000))
                .build();
        VoucherEntity voucher = VoucherEntity.builder()
                .id("voucher-1")
                .code("SALE10")
                .build();

        when(productRepository.findById("product-1")).thenReturn(Optional.of(product));
        when(shippingRuleService.calculateShippingFee(any(), any())).thenReturn(BigDecimal.ZERO);
        when(voucherService.validateAndCalculate("SALE10", "user-1", BigDecimal.valueOf(100_000)))
                .thenReturn(VoucherCalculationResult.builder()
                        .voucher(voucher)
                        .discountAmount(BigDecimal.TEN)
                        .build());
        when(orderRepository.save(any(OrderEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        useCase.execute(request);

        verify(voucherService).markVoucherUsed("voucher-1");
    }
}
