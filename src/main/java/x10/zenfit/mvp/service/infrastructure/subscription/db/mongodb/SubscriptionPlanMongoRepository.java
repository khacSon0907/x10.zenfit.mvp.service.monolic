package x10.zenfit.mvp.service.infrastructure.subscription.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.common.domain.enums.SubscriptionPlanStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubscriptionPlanMongoRepository extends MongoRepository<SubscriptionPlanDocument, String> {

    Optional<SubscriptionPlanDocument> findByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCaseAndIdNot(String code, String id);

    Optional<SubscriptionPlanDocument> findFirstByIsDefaultTrue();

    List<SubscriptionPlanDocument> findByStatusOrderByDisplayOrderAsc(SubscriptionPlanStatus status);
}

