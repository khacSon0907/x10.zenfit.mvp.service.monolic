package x10.zenfit.mvp.service.service.orderService.getOrderUc;

import x10.zenfit.mvp.service.service.orderService.OrderResponse;

import java.util.List;

public interface IGetAllOrdersUc {
    List<OrderResponse> execute();

    OrderCursorPageResponse executeByCursor(String cursor, Integer limit);
}
