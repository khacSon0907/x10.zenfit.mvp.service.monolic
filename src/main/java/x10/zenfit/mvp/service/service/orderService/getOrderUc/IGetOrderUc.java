package x10.zenfit.mvp.service.service.orderService.getOrderUc;

import x10.zenfit.mvp.service.service.orderService.OrderResponse;

import java.util.List;

public interface IGetOrderUc {
    OrderResponse execute(String orderId);
    OrderResponse executeByOrderCode(String orderCode);
    List<OrderResponse> executeLookupByKeyword(String keyword);
    OrderResponse executeGuestLookup(GuestOrderLookupRequest request);
}
