package x10.zenfit.mvp.service.infrastructure.order.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.Repository.IOrderRepository;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;
import x10.zenfit.mvp.service.common.domain.enums.OrderStatus;
import x10.zenfit.mvp.service.infrastructure.order.db.mongodb.OrderDocument;
import x10.zenfit.mvp.service.infrastructure.order.db.mongodb.OrderMongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements IOrderRepository {

    private final OrderMongoRepository orderMongoRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public OrderEntity save(OrderEntity order) {
        OrderDocument document = OrderMapper.toDocument(order);
        OrderDocument saved = orderMongoRepository.save(document);
        return OrderMapper.toEntity(saved);
    }

    @Override
    public List<OrderEntity> findAll() {
        return orderMongoRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(OrderMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderEntity> findAllByCursor(LocalDateTime cursorCreatedAt, String cursorId, int limit) {
        Query query = createCursorQuery(cursorCreatedAt, cursorId, limit);

        return mongoTemplate.find(query, OrderDocument.class).stream()
                .map(OrderMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderEntity> findByUserId(String userId) {
        return orderMongoRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(OrderMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderEntity> findByUserIdByCursor(String userId, LocalDateTime cursorCreatedAt, String cursorId, int limit) {
        Query query = createCursorQuery(cursorCreatedAt, cursorId, limit);
        query.addCriteria(Criteria.where("userId").is(userId));

        return mongoTemplate.find(query, OrderDocument.class).stream()
                .map(OrderMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderEntity> findById(String id) {
        return orderMongoRepository.findById(id)
                .map(OrderMapper::toEntity);
    }

    @Override
    public Optional<OrderEntity> findByOrderCode(String orderCode) {
        return orderMongoRepository.findByOrderCode(orderCode)
                .map(OrderMapper::toEntity);
    }

    @Override
    public List<OrderEntity> findByReceiverPhone(String receiverPhone) {
        return orderMongoRepository.findByReceiverPhoneOrderByCreatedAtDesc(receiverPhone).stream()
                .map(OrderMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderEntity> findByPayosOrderCode(Long payosOrderCode) {
        return orderMongoRepository.findByPayosOrderCode(payosOrderCode)
                .map(OrderMapper::toEntity);
    }

    @Override
    public List<OrderEntity> findByStatus(OrderStatus status) {
        return orderMongoRepository.findByStatus(status).stream()
                .map(OrderMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderEntity> findByStatusAndCreatedAtBetween(OrderStatus status, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        return orderMongoRepository.findByStatusAndCreatedAtBetween(status, startDateTime, endDateTime).stream()
                .map(OrderMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        orderMongoRepository.deleteById(id);
    }

    private Query createCursorQuery(LocalDateTime cursorCreatedAt, String cursorId, int limit) {
        Query query = new Query()
                .with(Sort.by(
                        Sort.Order.desc("createdAt"),
                        Sort.Order.desc("_id")
                ))
                .limit(limit);

        if (cursorCreatedAt != null && cursorId != null && !cursorId.isBlank()) {
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("createdAt").lt(cursorCreatedAt),
                    new Criteria().andOperator(
                            Criteria.where("createdAt").is(cursorCreatedAt),
                            Criteria.where("_id").lt(cursorId)
                    )
            ));
        }

        return query;
    }
}
