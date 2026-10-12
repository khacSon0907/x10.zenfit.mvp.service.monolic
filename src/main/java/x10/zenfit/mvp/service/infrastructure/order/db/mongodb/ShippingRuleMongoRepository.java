package x10.zenfit.mvp.service.infrastructure.order.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ShippingRuleMongoRepository extends MongoRepository<ShippingRuleDocument, String> {
    Optional<ShippingRuleDocument> findFirstByActiveTrueOrderByUpdatedAtDesc();
    List<ShippingRuleDocument> findByActiveTrue();
}
