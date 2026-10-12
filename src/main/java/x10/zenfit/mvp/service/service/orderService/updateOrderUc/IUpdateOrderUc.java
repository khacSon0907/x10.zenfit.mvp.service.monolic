package x10.zenfit.mvp.service.service.orderService.updateOrderUc;

import x10.zenfit.mvp.service.service.orderService.OrderResponse;

public interface IUpdateOrderUc {
    OrderResponse execute(String orderId, UpdateOrderRequest request);
}
