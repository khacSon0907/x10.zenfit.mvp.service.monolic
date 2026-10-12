package x10.zenfit.mvp.service.infrastructure.voucher.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.Repository.IVoucherRepository;
import x10.zenfit.mvp.service.common.domain.entities.voucher.VoucherEntity;
import x10.zenfit.mvp.service.common.domain.enums.VoucherSource;
import x10.zenfit.mvp.service.infrastructure.voucher.db.mongodb.VoucherMongoRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VoucherRepositoryImpl implements IVoucherRepository {

    private final VoucherMongoRepository voucherMongoRepository;

    @Override
    public VoucherEntity save(VoucherEntity voucher) {
        return VoucherMapper.toEntity(voucherMongoRepository.save(VoucherMapper.toDocument(voucher)));
    }

    @Override
    public Optional<VoucherEntity> findById(String id) {
        return voucherMongoRepository.findById(id).map(VoucherMapper::toEntity);
    }

    @Override
    public Optional<VoucherEntity> findByCode(String code) {
        return voucherMongoRepository.findByCode(normalizeCode(code)).map(VoucherMapper::toEntity);
    }

    @Override
    public List<VoucherEntity> findAll() {
        return voucherMongoRepository.findAll().stream().map(VoucherMapper::toEntity).toList();
    }

    @Override
    public List<VoucherEntity> findByAssignedUserId(String userId) {
        return voucherMongoRepository.findByAssignedUserId(userId).stream().map(VoucherMapper::toEntity).toList();
    }

    @Override
    public boolean existsByCode(String code) {
        return voucherMongoRepository.existsByCode(normalizeCode(code));
    }

    @Override
    public boolean existsByAssignedUserIdAndSourceAndEventName(String userId, VoucherSource source, String eventName) {
        return voucherMongoRepository.existsByAssignedUserIdAndSourceAndEventName(userId, source, eventName);
    }

    @Override
    public void deleteById(String id) {
        voucherMongoRepository.deleteById(id);
    }

    private String normalizeCode(String code) {
        return code == null ? null : code.trim().toUpperCase();
    }
}
