package x10.zenfit.mvp.service.service.userSubscriptionService.getSubscriptionByIdUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.ISubscriptionPlanRepository;
import x10.zenfit.mvp.service.Repository.IUserSubscriptionRepository;
import x10.zenfit.mvp.service.common.domain.entities.subscription.SubscriptionPlanEntity;
import x10.zenfit.mvp.service.common.domain.entities.subscription.UserSubscriptionEntity;
import x10.zenfit.mvp.service.service.userSubscriptionService.UserSubscriptionResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.subscription.UserSubscriptionError;

@Component
@RequiredArgsConstructor
public class GetSubscriptionByIdUcImpl implements IGetSubscriptionByIdUc {

    private final IUserSubscriptionRepository userSubscriptionRepository;
    private final ISubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    public UserSubscriptionResponse execute(String id, String currentUserId, boolean isManagement) {
        if (id == null || id.isBlank()) {
            throw new BusinessException(UserSubscriptionError.SUBSCRIPTION_NOT_FOUND);
        }

        UserSubscriptionEntity subscription = userSubscriptionRepository.findById(id.trim())
                .orElseThrow(() -> new BusinessException(UserSubscriptionError.SUBSCRIPTION_NOT_FOUND));

        if (!isManagement && !subscription.getUserId().equals(currentUserId)) {
            throw new BusinessException(UserSubscriptionError.ACCESS_DENIED);
        }

        SubscriptionPlanEntity plan = subscriptionPlanRepository.findById(subscription.getPlanId()).orElse(null);
        return UserSubscriptionResponse.fromEntity(subscription, plan);
    }
}

