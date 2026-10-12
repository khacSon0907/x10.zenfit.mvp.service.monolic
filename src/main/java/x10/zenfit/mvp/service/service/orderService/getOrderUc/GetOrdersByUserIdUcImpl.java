package x10.zenfit.mvp.service.service.orderService.getOrderUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IOrderRepository;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;
import x10.zenfit.mvp.service.service.orderService.OrderResponse;
import x10.zenfit.mvp.service.service.orderService.OrderResponseMapper;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.order.OrderError;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GetOrdersByUserIdUcImpl implements IGetOrdersByUserIdUc {

    private static final int DEFAULT_LIMIT = 20;
    private static final int MAX_LIMIT = 100;
    private static final String CURSOR_SEPARATOR = "|";

    private final IOrderRepository orderRepository;

    @Override
    public List<OrderResponse> execute(String userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(OrderResponseMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    public OrderCursorPageResponse executeByCursor(String userId, String cursor, Integer limit) {
        int pageSize = normalizeLimit(limit);
        CursorValue cursorValue = decodeCursor(cursor);

        List<OrderEntity> orders = orderRepository.findByUserIdByCursor(
                userId,
                cursorValue.createdAt(),
                cursorValue.id(),
                pageSize + 1
        );

        boolean hasNext = orders.size() > pageSize;
        List<OrderEntity> items = hasNext ? orders.subList(0, pageSize) : orders;
        OrderEntity lastItem = items.isEmpty() ? null : items.get(items.size() - 1);

        return OrderCursorPageResponse.builder()
                .items(items.stream()
                        .map(OrderResponseMapper::toResponse)
                        .collect(Collectors.toList()))
                .nextCursor(hasNext ? encodeCursor(lastItem) : null)
                .hasNext(hasNext)
                .limit(pageSize)
                .build();
    }

    private int normalizeLimit(Integer limit) {
        if (limit == null || limit < 1) {
            return DEFAULT_LIMIT;
        }

        return Math.min(limit, MAX_LIMIT);
    }

    private CursorValue decodeCursor(String cursor) {
        if (cursor == null || cursor.isBlank()) {
            return new CursorValue(null, null);
        }

        try {
            String decoded = new String(
                    Base64.getUrlDecoder().decode(cursor),
                    StandardCharsets.UTF_8
            );
            String[] parts = decoded.split("\\" + CURSOR_SEPARATOR, 2);
            if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
                throw new IllegalArgumentException("Invalid cursor");
            }

            return new CursorValue(LocalDateTime.parse(parts[0]), parts[1]);
        } catch (Exception e) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA, "Cursor phan trang khong hop le");
        }
    }

    private String encodeCursor(OrderEntity order) {
        String rawCursor = order.getCreatedAt() + CURSOR_SEPARATOR + order.getId();
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(rawCursor.getBytes(StandardCharsets.UTF_8));
    }

    private record CursorValue(LocalDateTime createdAt, String id) {
    }
}
