package x10.zenfit.mvp.service.infrastructure.order.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import x10.zenfit.mvp.service.common.domain.enums.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderMongoRepository extends MongoRepository<OrderDocument, String> {
    List<OrderDocument> findAllByOrderByCreatedAtDesc();
    List<OrderDocument> findByUserIdOrderByCreatedAtDesc(String userId);
    Optional<OrderDocument> findByOrderCode(String orderCode);
    List<OrderDocument> findByReceiverPhoneOrderByCreatedAtDesc(String receiverPhone);
    Optional<OrderDocument> findByPayosOrderCode(Long payosOrderCode);
    List<OrderDocument> findByStatus(OrderStatus status);
    List<OrderDocument> findByStatusAndCreatedAtBetween(OrderStatus status, LocalDateTime startDateTime, LocalDateTime endDateTime);
}
