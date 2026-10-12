package x10.zenfit.mvp.service.service.orderService;

import x10.zenfit.mvp.service.service.orderService.createOrderUc.CreateOrderRequest;
import x10.zenfit.mvp.service.service.orderService.getOrderUc.GuestOrderLookupRequest;
import x10.zenfit.mvp.service.service.orderService.getOrderUc.OrderCursorPageResponse;
import x10.zenfit.mvp.service.service.orderService.updateOrderUc.UpdateOrderRequest;

import java.util.List;

public interface ICoreOrderService {
    OrderResponse createOrder(CreateOrderRequest request);
    List<OrderResponse> getAllOrders();
    OrderCursorPageResponse getAllOrdersByCursor(String cursor, Integer limit);
    List<OrderResponse> getOrdersByUserId(String userId);
    OrderCursorPageResponse getOrdersByUserIdByCursor(String userId, String cursor, Integer limit);
    OrderResponse getOrder(String orderId);
    OrderResponse lookupOrderByCode(String orderCode);
    List<OrderResponse> lookupOrders(String keyword);
    OrderResponse lookupGuestOrder(GuestOrderLookupRequest request);
    OrderResponse updateOrder(String orderId, UpdateOrderRequest request);
    void deleteOrder(String orderId);
}
