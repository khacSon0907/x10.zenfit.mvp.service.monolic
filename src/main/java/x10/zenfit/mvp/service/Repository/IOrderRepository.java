package x10.zenfit.mvp.service.Repository;

import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;
import x10.zenfit.mvp.service.common.domain.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IOrderRepository {

    OrderEntity save(OrderEntity order);

    List<OrderEntity> findAll();

    List<OrderEntity> findAllByCursor(LocalDateTime cursorCreatedAt, String cursorId, int limit);

    List<OrderEntity> findByUserId(String userId);

    List<OrderEntity> findByUserIdByCursor(String userId, LocalDateTime cursorCreatedAt, String cursorId, int limit);

    Optional<OrderEntity> findById(String id);

    Optional<OrderEntity> findByOrderCode(String orderCode);

    List<OrderEntity> findByReceiverPhone(String receiverPhone);

    Optional<OrderEntity> findByPayosOrderCode(Long payosOrderCode);

    List<OrderEntity> findByStatus(OrderStatus status);

    List<OrderEntity> findByStatusAndCreatedAtBetween(OrderStatus status, LocalDateTime startDateTime, LocalDateTime endDateTime);

    void deleteById(String id);
}
