package x10.zenfit.mvp.service.service.userSubscriptionService.getMySubscriptionUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.ISubscriptionPlanRepository;
import x10.zenfit.mvp.service.Repository.IUserSubscriptionRepository;
import x10.zenfit.mvp.service.common.domain.entities.subscription.SubscriptionPlanEntity;
import x10.zenfit.mvp.service.common.domain.entities.subscription.UserSubscriptionEntity;
import x10.zenfit.mvp.service.service.userSubscriptionService.UserSubscriptionResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.subscription.UserSubscriptionError;
import x10.zenfit.mvp.service.share.exception.user.UserError;

@Component
@RequiredArgsConstructor
public class GetMySubscriptionUcImpl implements IGetMySubscriptionUc {

    private final IUserSubscriptionRepository userSubscriptionRepository;
    private final ISubscriptionPlanRepository subscriptionPlanRepository;

    @Override
    public UserSubscriptionResponse execute(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new BusinessException(UserError.USER_NOT_FOUND);
        }

        UserSubscriptionEntity subscription = userSubscriptionRepository.findActiveSubscriptionByUserId(userId.trim())
                .orElseThrow(() -> new BusinessException(UserSubscriptionError.NO_ACTIVE_SUBSCRIPTION));

        SubscriptionPlanEntity plan = subscriptionPlanRepository.findById(subscription.getPlanId())
                .orElse(null);

        return UserSubscriptionResponse.fromEntity(subscription, plan);
    }
}

