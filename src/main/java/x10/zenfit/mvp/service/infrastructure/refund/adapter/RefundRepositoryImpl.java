package x10.zenfit.mvp.service.infrastructure.refund.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.Repository.IRefundRepository;
import x10.zenfit.mvp.service.common.domain.entities.order.RefundEntity;
import x10.zenfit.mvp.service.common.domain.enums.RefundStatus;
import x10.zenfit.mvp.service.infrastructure.refund.db.mongodb.RefundDocument;
import x10.zenfit.mvp.service.infrastructure.refund.db.mongodb.RefundMongoRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RefundRepositoryImpl implements IRefundRepository {

    private final RefundMongoRepository refundMongoRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public RefundEntity save(RefundEntity refund) {
        return RefundMapper.toEntity(refundMongoRepository.save(RefundMapper.toDocument(refund)));
    }

    @Override
    public Optional<RefundEntity> findById(String id) {
        return refundMongoRepository.findById(id).map(RefundMapper::toEntity);
    }

    @Override
    public List<RefundEntity> findAll() {
        return refundMongoRepository.findAllByOrderByCreatedAtDesc().stream()
                .map(RefundMapper::toEntity)
                .toList();
    }

    @Override
    public List<RefundEntity> findAllByCursor(LocalDateTime cursorCreatedAt, String cursorId, int limit) {
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

        return mongoTemplate.find(query, RefundDocument.class).stream()
                .map(RefundMapper::toEntity)
                .toList();
    }

    @Override
    public List<RefundEntity> findByUserId(String userId) {
        return refundMongoRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(RefundMapper::toEntity)
                .toList();
    }

    @Override
    public List<RefundEntity> findByOrderId(String orderId) {
        return refundMongoRepository.findByOrderIdOrderByCreatedAtDesc(orderId).stream()
                .map(RefundMapper::toEntity)
                .toList();
    }

    @Override
    public List<RefundEntity> findByStatus(RefundStatus status) {
        return refundMongoRepository.findByStatusOrderByCreatedAtDesc(status).stream()
                .map(RefundMapper::toEntity)
                .toList();
    }
}
