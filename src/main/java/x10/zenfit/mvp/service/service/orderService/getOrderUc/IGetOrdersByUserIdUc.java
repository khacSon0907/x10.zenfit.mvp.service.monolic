package x10.zenfit.mvp.service.service.orderService.getOrderUc;

import x10.zenfit.mvp.service.service.orderService.OrderResponse;

import java.util.List;

public interface IGetOrdersByUserIdUc {
    List<OrderResponse> execute(String userId);

    OrderCursorPageResponse executeByCursor(String userId, String cursor, Integer limit);
}
