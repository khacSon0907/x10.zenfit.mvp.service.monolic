package x10.zenfit.mvp.service.Repository;

import x10.zenfit.mvp.service.common.domain.entities.voucher.VoucherEntity;
import x10.zenfit.mvp.service.common.domain.enums.VoucherSource;

import java.util.List;
import java.util.Optional;

public interface IVoucherRepository {
    VoucherEntity save(VoucherEntity voucher);
    Optional<VoucherEntity> findById(String id);
    Optional<VoucherEntity> findByCode(String code);
    List<VoucherEntity> findAll();
    List<VoucherEntity> findByAssignedUserId(String userId);
    boolean existsByCode(String code);
    boolean existsByAssignedUserIdAndSourceAndEventName(String userId, VoucherSource source, String eventName);
    void deleteById(String id);
}
