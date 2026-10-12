package x10.zenfit.mvp.service.service.orderService.deleteOrderUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IOrderRepository;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.order.OrderError;

@Service
@RequiredArgsConstructor
public class DeleteOrderUcImpl implements IDeleteOrderUc {

    private final IOrderRepository orderRepository;

    @Override
    public void execute(String orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .or(() -> orderRepository.findByOrderCode(orderId))
                .orElseThrow(() -> new BusinessException(OrderError.ORDER_NOT_FOUND));

        orderRepository.deleteById(order.getId());
    }
}
