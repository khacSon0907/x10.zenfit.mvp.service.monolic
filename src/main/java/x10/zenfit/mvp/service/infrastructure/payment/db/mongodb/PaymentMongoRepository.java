package x10.zenfit.mvp.service.infrastructure.payment.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PaymentMongoRepository extends MongoRepository<PaymentDocument, String> {

    Optional<PaymentDocument> findByOrderId(String orderId);

    Optional<PaymentDocument> findByProviderOrderCode(Long providerOrderCode);
}
