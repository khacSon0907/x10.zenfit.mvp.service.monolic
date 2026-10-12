package x10.zenfit.mvp.service.service.orderService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IOrderRepository;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;
import x10.zenfit.mvp.service.common.domain.enums.OrderStatus;
import x10.zenfit.mvp.service.service.pointService.ICorePointService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderStatusScheduler {

    private final IOrderRepository orderRepository;
    private final ICorePointService pointService;

    @Value("${app.order.auto-status.enabled:true}")
    private boolean enabled;

    @Value("${app.order.auto-status.confirmed-to-packing-minutes:5}")
    private long confirmedToPackingMinutes;

    @Value("${app.order.auto-status.packing-to-shipping-minutes:1440}")
    private long packingToShippingMinutes;

    @Value("${app.order.auto-status.shipping-to-completed-minutes:4320}")
    private long shippingToCompletedMinutes;

    @Scheduled(
            initialDelayString = "${app.order.auto-status.initial-delay-ms:60000}",
            fixedDelayString = "${app.order.auto-status.fixed-delay-ms:300000}"
    )
    public void autoAdvanceOrderStatuses() {
        if (!enabled) {
            return;
        }

        LocalDateTime now = LocalDateTime.now();
        advanceOrders(OrderStatus.CONFIRMED, OrderStatus.PACKING, now.minusMinutes(confirmedToPackingMinutes), now);
        advanceOrders(OrderStatus.PACKING, OrderStatus.SHIPPING, now.minusMinutes(packingToShippingMinutes), now);
        advanceOrders(OrderStatus.SHIPPING, OrderStatus.COMPLETED, now.minusMinutes(shippingToCompletedMinutes), now);
    }

    private void advanceOrders(OrderStatus currentStatus, OrderStatus nextStatus, LocalDateTime cutoff, LocalDateTime now) {
        List<OrderEntity> orders = orderRepository.findByStatus(currentStatus);

        for (OrderEntity order : orders) {
            LocalDateTime statusChangedAt = resolveStatusChangedAt(order);
            if (statusChangedAt == null || statusChangedAt.isAfter(cutoff)) {
                continue;
            }

            order.setStatus(nextStatus);
            if (nextStatus == OrderStatus.COMPLETED) {
                pointService.awardPointsForCompletedOrder(order);
            }
            order.setUpdatedAt(now);
            orderRepository.save(order);

            log.info(
                    "Auto advanced order {} from {} to {}",
                    order.getOrderCode() == null ? order.getId() : order.getOrderCode(),
                    currentStatus,
                    nextStatus
            );
        }
    }

    private LocalDateTime resolveStatusChangedAt(OrderEntity order) {
        if (order.getUpdatedAt() != null) {
            return order.getUpdatedAt();
        }

        return order.getCreatedAt();
    }
}
