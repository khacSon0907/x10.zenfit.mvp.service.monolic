package x10.zenfit.mvp.service.Repository;

import x10.zenfit.mvp.service.common.domain.entities.subscription.SubscriptionPlanEntity;
import x10.zenfit.mvp.service.common.domain.enums.SubscriptionPlanStatus;

import java.util.List;
import java.util.Optional;

public interface ISubscriptionPlanRepository {

    SubscriptionPlanEntity save(SubscriptionPlanEntity plan);

    Optional<SubscriptionPlanEntity> findById(String id);

    Optional<SubscriptionPlanEntity> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, String id);

    Optional<SubscriptionPlanEntity> findDefaultPlan();

    void clearDefaultFlagExcept(String planId);

    List<SubscriptionPlanEntity> findActivePlans();

    PaginatedResult<SubscriptionPlanEntity> findAdminPlans(
            String keyword,
            SubscriptionPlanStatus status,
            int page,
            int size
    );

    void deleteById(String id);
}

