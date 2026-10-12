package x10.zenfit.mvp.service.infrastructure.voucher.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import x10.zenfit.mvp.service.common.domain.enums.VoucherSource;

import java.util.List;
import java.util.Optional;

public interface VoucherMongoRepository extends MongoRepository<VoucherDocument, String> {
    Optional<VoucherDocument> findByCode(String code);
    List<VoucherDocument> findByAssignedUserId(String assignedUserId);
    boolean existsByCode(String code);
    boolean existsByAssignedUserIdAndSourceAndEventName(String assignedUserId, VoucherSource source, String eventName);
}
