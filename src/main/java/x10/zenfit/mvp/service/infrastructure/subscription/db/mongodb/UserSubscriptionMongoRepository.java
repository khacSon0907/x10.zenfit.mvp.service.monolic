package x10.zenfit.mvp.service.infrastructure.subscription.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.common.domain.enums.UserSubscriptionStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserSubscriptionMongoRepository extends MongoRepository<UserSubscriptionDocument, String> {

    Optional<UserSubscriptionDocument> findFirstByUserIdAndStatusOrderByCreatedAtDesc(
            String userId,
            UserSubscriptionStatus status
    );

    List<UserSubscriptionDocument> findByUserIdOrderByCreatedAtDesc(String userId);

    boolean existsByUserIdAndStatus(String userId, UserSubscriptionStatus status);
}

