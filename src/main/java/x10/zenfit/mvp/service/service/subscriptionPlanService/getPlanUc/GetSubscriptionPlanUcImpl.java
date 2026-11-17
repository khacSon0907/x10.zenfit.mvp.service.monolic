package x10.zenfit.mvp.service.service.subscriptionPlanService.getPlanUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.ISubscriptionPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.subscription.SubscriptionPlanEntity;
import x10.zenfit.mvp.service.service.subscriptionPlanService.SubscriptionPlanResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.subscription.SubscriptionPlanError;

@Component
@RequiredArgsConstructor
public class GetSubscriptionPlanUcImpl implements IGetSubscriptionPlanUc {

    private final ISubscriptionPlanRepository planRepository;

    @Override
    public SubscriptionPlanResponse getById(String id) {
        if (id == null || id.isBlank()) {
            throw new BusinessException(SubscriptionPlanError.PLAN_NOT_FOUND);
        }

        SubscriptionPlanEntity plan = planRepository.findById(id.trim())
                .orElseThrow(() -> new BusinessException(SubscriptionPlanError.PLAN_NOT_FOUND));

        return SubscriptionPlanResponse.fromEntity(plan);
    }

    @Override
    public SubscriptionPlanResponse getDefaultPlan() {
        SubscriptionPlanEntity defaultPlan = planRepository.findDefaultPlan()
                .orElseThrow(() -> new BusinessException(SubscriptionPlanError.PLAN_NOT_FOUND));

        return SubscriptionPlanResponse.fromEntity(defaultPlan);
    }
}

