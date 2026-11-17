package x10.zenfit.mvp.service.service.userSubscriptionService.getMySubscriptionHistoryUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.ISubscriptionPlanRepository;
import x10.zenfit.mvp.service.Repository.IUserSubscriptionRepository;
import x10.zenfit.mvp.service.common.domain.entities.subscription.SubscriptionPlanEntity;
import x10.zenfit.mvp.service.common.domain.entities.subscription.UserSubscriptionEntity;
import x10.zenfit.mvp.service.service.userSubscriptionService.UserSubscriptionResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.user.UserError;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetMySubscriptionHistoryUcImpl implements IGetMySubscriptionHistoryUc {

    private final IUserSubscriptionRepository userSubscriptionRepository;
    private final ISubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    public List<UserSubscriptionResponse> execute(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new BusinessException(UserError.USER_NOT_FOUND);
        }

        List<UserSubscriptionEntity> subscriptions = userSubscriptionRepository.findByUserIdOrderByCreatedAtDesc(userId.trim());

        Map<String, SubscriptionPlanEntity> planMap = subscriptionPlanRepository.findActivePlans()
                .stream()
                .collect(Collectors.toMap(SubscriptionPlanEntity::getId, Function.identity(), (a, b) -> a));

        return subscriptions.stream()
                .map(sub -> {
                    SubscriptionPlanEntity plan = planMap.computeIfAbsent(sub.getPlanId(), id ->
                            subscriptionPlanRepository.findById(id).orElse(null)
                    );
                    return UserSubscriptionResponse.fromEntity(sub, plan);
                })
                .toList();
    }
}

