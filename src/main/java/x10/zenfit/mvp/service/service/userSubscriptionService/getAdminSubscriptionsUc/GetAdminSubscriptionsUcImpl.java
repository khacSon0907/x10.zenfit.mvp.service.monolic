package x10.zenfit.mvp.service.service.userSubscriptionService.getAdminSubscriptionsUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.ISubscriptionPlanRepository;
import x10.zenfit.mvp.service.Repository.IUserSubscriptionRepository;
import x10.zenfit.mvp.service.Repository.PaginatedResult;
import x10.zenfit.mvp.service.common.domain.entities.subscription.SubscriptionPlanEntity;
import x10.zenfit.mvp.service.common.domain.entities.subscription.UserSubscriptionEntity;
import x10.zenfit.mvp.service.service.userSubscriptionService.UserSubscriptionResponse;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetAdminSubscriptionsUcImpl implements IGetAdminSubscriptionsUc {

    private final IUserSubscriptionRepository userSubscriptionRepository;
    private final ISubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    public PaginatedResult<UserSubscriptionResponse> execute(UserSubscriptionFilterQuery query) {
        String userId = query != null ? query.getUserId() : null;
        String planId = query != null ? query.getPlanId() : null;
        var status = query != null ? query.getStatus() : null;
        int page = query != null && query.getPage() != null ? query.getPage() : 0;
        int size = query != null && query.getSize() != null ? query.getSize() : 10;

        PaginatedResult<UserSubscriptionEntity> entityResult = userSubscriptionRepository.findSubscriptions(
                userId,
                planId,
                status,
                page,
                size
        );

        Map<String, SubscriptionPlanEntity> planMap = subscriptionPlanRepository.findActivePlans()
                .stream()
                .collect(Collectors.toMap(SubscriptionPlanEntity::getId, Function.identity(), (a, b) -> a));

        List<UserSubscriptionResponse> items = entityResult.getItems().stream()
                .map(sub -> {
                    SubscriptionPlanEntity plan = planMap.computeIfAbsent(sub.getPlanId(), id ->
                            subscriptionPlanRepository.findById(id).orElse(null)
                    );
                    return UserSubscriptionResponse.fromEntity(sub, plan);
                })
                .toList();

        return PaginatedResult.<UserSubscriptionResponse>builder()
                .items(items)
                .page(entityResult.getPage())
                .size(entityResult.getSize())
                .totalElements(entityResult.getTotalElements())
                .totalPages(entityResult.getTotalPages())
                .hasNext(entityResult.isHasNext())
                .hasPrevious(entityResult.isHasPrevious())
                .build();
    }
}

