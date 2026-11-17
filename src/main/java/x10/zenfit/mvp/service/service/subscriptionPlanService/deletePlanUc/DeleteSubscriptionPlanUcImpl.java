package x10.zenfit.mvp.service.service.subscriptionPlanService.deletePlanUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import x10.zenfit.mvp.service.Repository.ISubscriptionPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.subscription.SubscriptionPlanEntity;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.subscription.SubscriptionPlanError;

@Component
@RequiredArgsConstructor
public class DeleteSubscriptionPlanUcImpl implements IDeleteSubscriptionPlanUc {

    private final ISubscriptionPlanRepository planRepository;

    @Override
    @Transactional
    public void execute(String id) {
        if (id == null || id.isBlank()) {
            throw new BusinessException(SubscriptionPlanError.PLAN_NOT_FOUND);
        }

        SubscriptionPlanEntity plan = planRepository.findById(id.trim())
                .orElseThrow(() -> new BusinessException(SubscriptionPlanError.PLAN_NOT_FOUND));

        if (Boolean.TRUE.equals(plan.getIsDefault())) {
            throw new BusinessException(SubscriptionPlanError.CANNOT_DELETE_DEFAULT_PLAN);
        }

        planRepository.deleteById(plan.getId());
    }
}

