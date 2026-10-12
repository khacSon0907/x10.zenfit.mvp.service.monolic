package x10.zenfit.mvp.service.service.orderService.getOrderUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import x10.zenfit.mvp.service.Repository.IOrderRepository;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;
import x10.zenfit.mvp.service.service.orderService.OrderResponse;
import x10.zenfit.mvp.service.service.orderService.OrderResponseMapper;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.order.OrderError;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetOrderUcImpl implements IGetOrderUc {

    private final IOrderRepository orderRepository;

    @Override
    public OrderResponse execute(String orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .or(() -> orderRepository.findByOrderCode(orderId))
                .orElseThrow(() -> new BusinessException(OrderError.ORDER_NOT_FOUND));

        return OrderResponseMapper.toResponse(order);
    }

    @Override
    public OrderResponse executeByOrderCode(String orderCode) {
        if (!StringUtils.hasText(orderCode)) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA, "Can co ma don hang");
        }

        OrderEntity order = orderRepository.findByOrderCode(orderCode.trim())
                .orElseThrow(() -> new BusinessException(OrderError.ORDER_NOT_FOUND));

        return OrderResponseMapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> executeLookupByKeyword(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA, "Can co ma don hang hoac so dien thoai");
        }

        String normalizedKeyword = keyword.trim();
        if (isOrderCode(normalizedKeyword)) {
            OrderEntity order = orderRepository.findByOrderCode(normalizedKeyword)
                    .orElseThrow(() -> new BusinessException(OrderError.ORDER_NOT_FOUND));
            return List.of(OrderResponseMapper.toResponse(order));
        }

        String receiverPhone = normalizePhone(normalizedKeyword);
        if (!StringUtils.hasText(receiverPhone)) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA, "So dien thoai khong hop le");
        }

        List<OrderResponse> orders = orderRepository.findByReceiverPhone(receiverPhone).stream()
                .map(OrderResponseMapper::toResponse)
                .toList();

        if (orders.isEmpty()) {
            throw new BusinessException(OrderError.ORDER_NOT_FOUND);
        }

        return orders;
    }

    @Override
    public OrderResponse executeGuestLookup(GuestOrderLookupRequest request) {
        if (request == null || !StringUtils.hasText(request.getOrderCode())) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA, "Can co ma don hang");
        }

        String email = normalizeEmail(request.getEmail());
        String receiverPhone = normalizePhone(request.getReceiverPhone());
        if (!StringUtils.hasText(email) && !StringUtils.hasText(receiverPhone)) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA, "Can co email hoac so dien thoai nhan hang");
        }

        OrderEntity order = orderRepository.findByOrderCode(request.getOrderCode().trim())
                .orElseThrow(() -> new BusinessException(OrderError.ORDER_NOT_FOUND));

        if (!StringUtils.hasText(order.getGuestId())) {
            throw new BusinessException(OrderError.ORDER_NOT_FOUND);
        }

        boolean emailMatched = StringUtils.hasText(email)
                && email.equalsIgnoreCase(normalizeEmail(order.getGuestEmail()));
        boolean phoneMatched = StringUtils.hasText(receiverPhone)
                && receiverPhone.equals(normalizePhone(order.getReceiverPhone()));

        if (!emailMatched && !phoneMatched) {
            throw new BusinessException(OrderError.ORDER_NOT_FOUND);
        }

        return OrderResponseMapper.toResponse(order);
    }

    private boolean isOrderCode(String value) {
        return value != null && value.trim().toUpperCase().startsWith("ORD");
    }

    private String normalizeEmail(String value) {
        return StringUtils.hasText(value) ? value.trim().toLowerCase() : null;
    }

    private String normalizePhone(String value) {
        if (!StringUtils.hasText(value)) {
            return null;
        }

        return value.replaceAll("[^0-9+]", "");
    }
}
