package x10.zenfit.mvp.service.infrastructure.payment.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.Repository.IPaymentRepository;
import x10.zenfit.mvp.service.common.domain.entities.order.PaymentEntity;
import x10.zenfit.mvp.service.infrastructure.payment.db.mongodb.PaymentMongoRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements IPaymentRepository {

    private final PaymentMongoRepository paymentMongoRepository;

    @Override
    public PaymentEntity save(PaymentEntity payment) {
        return PaymentMapper.toEntity(paymentMongoRepository.save(PaymentMapper.toDocument(payment)));
    }

    @Override
    public Optional<PaymentEntity> findById(String id) {
        return paymentMongoRepository.findById(id).map(PaymentMapper::toEntity);
    }

    @Override
    public Optional<PaymentEntity> findByOrderId(String orderId) {
        return paymentMongoRepository.findByOrderId(orderId).map(PaymentMapper::toEntity);
    }

    @Override
    public Optional<PaymentEntity> findByProviderOrderCode(Long providerOrderCode) {
        return paymentMongoRepository.findByProviderOrderCode(providerOrderCode).map(PaymentMapper::toEntity);
    }

    @Override
    public List<PaymentEntity> findAll() {
        return paymentMongoRepository.findAll().stream()
                .map(PaymentMapper::toEntity)
                .toList();
    }
}
