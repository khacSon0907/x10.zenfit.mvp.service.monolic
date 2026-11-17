package x10.zenfit.mvp.service.Repository;

import x10.zenfit.mvp.service.common.domain.entities.subscription.UserSubscriptionEntity;
import x10.zenfit.mvp.service.common.domain.enums.UserSubscriptionStatus;

import java.util.List;
import java.util.Optional;

public interface IUserSubscriptionRepository {

    UserSubscriptionEntity save(UserSubscriptionEntity subscription);

    Optional<UserSubscriptionEntity> findById(String id);

    Optional<UserSubscriptionEntity> findActiveSubscriptionByUserId(String userId);

    List<UserSubscriptionEntity> findByUserIdOrderByCreatedAtDesc(String userId);

    PaginatedResult<UserSubscriptionEntity> findSubscriptions(
            String userId,
            String planId,
            UserSubscriptionStatus status,
            int page,
            int size
    );

    boolean existsByUserIdAndStatus(String userId, UserSubscriptionStatus status);

    void deleteById(String id);
}

