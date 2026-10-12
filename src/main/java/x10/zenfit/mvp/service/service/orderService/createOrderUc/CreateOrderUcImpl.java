package x10.zenfit.mvp.service.service.orderService.createOrderUc;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IGuestRepository;
import x10.zenfit.mvp.service.Repository.IOrderRepository;
import x10.zenfit.mvp.service.Repository.IProductRepository;
import x10.zenfit.mvp.service.Repository.IUserRepository;
import x10.zenfit.mvp.service.common.domain.entities.guest.GuestEntity;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderItem;
import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;
import x10.zenfit.mvp.service.common.domain.entities.user.UserEntity;
import x10.zenfit.mvp.service.common.domain.enums.OrderStatus;
import x10.zenfit.mvp.service.common.domain.enums.PaymentMethod;
import x10.zenfit.mvp.service.common.domain.enums.PaymentStatus;
import x10.zenfit.mvp.service.service.orderService.AdminOrderSocketService;
import x10.zenfit.mvp.service.service.orderService.ICoreShippingRuleService;
import x10.zenfit.mvp.service.service.orderService.OrderResponse;
import x10.zenfit.mvp.service.service.orderService.OrderResponseMapper;
import x10.zenfit.mvp.service.service.paymentService.ICorePaymentService;
import x10.zenfit.mvp.service.service.paymentService.createPayment.CreatePaymentLinkRequest;
import x10.zenfit.mvp.service.service.paymentService.createPayment.CreatePaymentLinkResponse;
import x10.zenfit.mvp.service.service.notification.event.OrderInvoiceEmailEvent;
import x10.zenfit.mvp.service.service.voucherService.ICoreVoucherService;
import x10.zenfit.mvp.service.service.voucherService.VoucherCalculationResult;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.order.OrderError;
import x10.zenfit.mvp.service.share.exception.product.ProductError;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateOrderUcImpl implements ICreateOrderUc {

    private static final DateTimeFormatter ORDER_CODE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    private final IOrderRepository orderRepository;
    private final IProductRepository productRepository;
    private final IGuestRepository guestRepository;
    private final IUserRepository userRepository;
    private final ICorePaymentService paymentService;
    private final ICoreShippingRuleService shippingRuleService;
    private final ICoreVoucherService voucherService;
    private final ApplicationEventPublisher eventPublisher;
    private final AdminOrderSocketService adminOrderSocketService;

    @Override
    public OrderResponse execute(CreateOrderRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA);
        }
        if (!hasUser(request) && request.getGuest() == null) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA, "Can co userId hoac thong tin guest");
        }

        LocalDateTime now = LocalDateTime.now();
        GuestEntity guest = resolveGuest(request, now);
        List<OrderItem> items = request.getItems().stream()
                .map(this::buildOrderItem)
                .collect(Collectors.toList());

        BigDecimal subtotal = calculateSubtotal(items);
        BigDecimal shippingFee = shippingRuleService.calculateShippingFee(subtotal, LocalDate.now());
        VoucherCalculationResult voucherResult = resolveVoucher(request, subtotal);
        BigDecimal discountAmount = voucherResult == null
                ? defaultZero(request.getDiscountAmount())
                : voucherResult.getDiscountAmount();
        BigDecimal totalAmount = subtotal.add(shippingFee).subtract(discountAmount);

        if (totalAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA, "Tong tien don hang khong hop le");
        }

        PaymentMethod paymentMethod = request.getPaymentMethod() == null
                ? PaymentMethod.COD
                : request.getPaymentMethod();

        OrderEntity order = OrderEntity.builder()
                .id(UUID.randomUUID().toString())
                .orderCode(generateOrderCode(now))
                .userId(request.getUserId())
                .guestId(guest == null ? null : guest.getId())
                .guestEmail(guest == null ? null : guest.getEmail())
                .guestUsername(guest == null ? null : guest.getUsername())
                .receiverName(request.getReceiverName())
                .receiverPhone(request.getReceiverPhone())
                .receiverAddress(request.getReceiverAddress())
                .items(items)
                .subtotal(subtotal)
                .shippingFee(shippingFee)
                .discountAmount(discountAmount)
                .totalAmount(totalAmount)
                .voucherId(voucherResult == null ? null : voucherResult.getVoucher().getId())
                .voucherCode(voucherResult == null ? null : voucherResult.getVoucher().getCode())
                .paymentMethod(paymentMethod)
                .paymentStatus(PaymentStatus.PENDING)
                .status(OrderStatus.PENDING)
                .note(request.getNote())
                .createdAt(now)
                .updatedAt(now)
                .build();

        OrderEntity savedOrder = orderRepository.save(order);
        if (voucherResult != null) {
            voucherService.markVoucherUsed(voucherResult.getVoucher().getId());
        }
        OrderResponse response = OrderResponseMapper.toResponse(savedOrder);
        publishInvoiceEmail(savedOrder);

        if (PaymentMethod.PAYOS == paymentMethod) {
            CreatePaymentLinkRequest paymentRequest = new CreatePaymentLinkRequest();
            paymentRequest.setOrderId(savedOrder.getId());
            CreatePaymentLinkResponse paymentResponse = paymentService.createPaymentLink(paymentRequest);
            response.setPayment(paymentResponse);
        }

        adminOrderSocketService.sendNewOrder(savedOrder);

        return response;
    }

    private boolean hasUser(CreateOrderRequest request) {
        return request.getUserId() != null && !request.getUserId().isBlank();
    }

    private GuestEntity resolveGuest(CreateOrderRequest request, LocalDateTime now) {
        if (hasUser(request)) {
            return null;
        }

        CreateOrderRequest.GuestRequest guestRequest = request.getGuest();
        if (guestRequest == null
                || guestRequest.getEmail() == null
                || guestRequest.getEmail().isBlank()
                || guestRequest.getUsername() == null
                || guestRequest.getUsername().isBlank()) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA, "Thong tin guest khong hop le");
        }

        String email = guestRequest.getEmail().trim().toLowerCase();
        String username = guestRequest.getUsername().trim();

        return guestRepository.findByEmail(email)
                .map(existingGuest -> updateGuestUsername(existingGuest, username))
                .orElseGet(() -> guestRepository.save(GuestEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .email(email)
                        .username(username)
                        .createdAt(now)
                        .build()));
    }

    private GuestEntity updateGuestUsername(GuestEntity guest, String username) {
        if (!username.equals(guest.getUsername())) {
            guest.setUsername(username);
            return guestRepository.save(guest);
        }

        return guest;
    }

    private void publishInvoiceEmail(OrderEntity order) {
        if (order.getGuestEmail() != null && !order.getGuestEmail().isBlank()) {
            eventPublisher.publishEvent(new OrderInvoiceEmailEvent(
                    order.getGuestEmail(),
                    order.getGuestUsername(),
                    order
            ));
            return;
        }

        if (order.getUserId() == null || order.getUserId().isBlank()) {
            return;
        }

        userRepository.findById(order.getUserId())
                .filter(user -> user.getEmail() != null && !user.getEmail().isBlank())
                .ifPresent(user -> eventPublisher.publishEvent(new OrderInvoiceEmailEvent(
                        user.getEmail(),
                        resolveUsername(user),
                        order
                )));
    }

    private String resolveUsername(UserEntity user) {
        if (user.getUsername() != null && !user.getUsername().isBlank()) {
            return user.getUsername();
        }
        return user.getEmail();
    }

    private OrderItem buildOrderItem(CreateOrderRequest.OrderItemRequest request) {
        ProductEntity product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new BusinessException(ProductError.PRODUCT_NOT_FOUND));

        BigDecimal unitPrice = product.getSalePrice() != null ? product.getSalePrice() : product.getPrice();
        if (unitPrice == null || unitPrice.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA, "Gia san pham khong hop le");
        }

        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(request.getQuantity()));

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
                .totalPrice(totalPrice)
                .build();
    }

    private BigDecimal calculateSubtotal(List<OrderItem> items) {
        return items.stream()
                .map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal defaultZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private VoucherCalculationResult resolveVoucher(CreateOrderRequest request, BigDecimal subtotal) {
        if (request.getVoucherCode() == null || request.getVoucherCode().isBlank()) {
            return null;
        }
        return voucherService.validateAndCalculate(request.getVoucherCode(), request.getUserId(), subtotal);
    }

    private String generateOrderCode(LocalDateTime now) {
        String suffix = UUID.randomUUID().toString().replace("-", "").substring(0, 6).toUpperCase();
        return "ORD" + now.format(ORDER_CODE_TIME_FORMAT) + suffix;
    }
}
