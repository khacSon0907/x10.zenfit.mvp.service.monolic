package x10.zenfit.mvp.service.Repository;

import x10.zenfit.mvp.service.common.domain.entities.order.RefundEntity;
import x10.zenfit.mvp.service.common.domain.enums.RefundStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface IRefundRepository {

    RefundEntity save(RefundEntity refund);

    Optional<RefundEntity> findById(String id);

    List<RefundEntity> findAll();

    List<RefundEntity> findAllByCursor(LocalDateTime cursorCreatedAt, String cursorId, int limit);

    List<RefundEntity> findByUserId(String userId);

    List<RefundEntity> findByOrderId(String orderId);

    List<RefundEntity> findByStatus(RefundStatus status);
}
