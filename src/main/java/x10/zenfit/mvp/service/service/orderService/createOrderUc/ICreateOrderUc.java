package x10.zenfit.mvp.service.service.orderService.createOrderUc;

import x10.zenfit.mvp.service.service.orderService.OrderResponse;

public interface ICreateOrderUc {
    OrderResponse execute(CreateOrderRequest request);
}
