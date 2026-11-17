package x10.zenfit.mvp.service.service.subscriptionPlanService.updatePlanStatusUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import x10.zenfit.mvp.service.Repository.ISubscriptionPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.subscription.SubscriptionPlanEntity;
import x10.zenfit.mvp.service.common.domain.enums.SubscriptionPlanStatus;
import x10.zenfit.mvp.service.service.subscriptionPlanService.SubscriptionPlanResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.subscription.SubscriptionPlanError;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UpdateSubscriptionPlanStatusUcImpl implements IUpdateSubscriptionPlanStatusUc {

    private final ISubscriptionPlanRepository planRepository;

    @Override
    @Transactional
    public SubscriptionPlanResponse execute(String id, UpdateSubscriptionPlanStatusRequest request) {
        if (id == null || id.isBlank()) {
            throw new BusinessException(SubscriptionPlanError.PLAN_NOT_FOUND);
        }

        if (request == null || request.getStatus() == null) {
            throw new BusinessException(SubscriptionPlanError.INVALID_PLAN_DATA);
        }

        SubscriptionPlanEntity plan = planRepository.findById(id.trim())
                .orElseThrow(() -> new BusinessException(SubscriptionPlanError.PLAN_NOT_FOUND));

        if (plan.getStatus() == SubscriptionPlanStatus.ARCHIVED && request.getStatus() != SubscriptionPlanStatus.ARCHIVED) {
            throw new BusinessException(SubscriptionPlanError.CANNOT_MODIFY_ARCHIVED_PLAN);
        }

        if (Boolean.TRUE.equals(plan.getIsDefault()) && request.getStatus() != SubscriptionPlanStatus.ACTIVE) {
            throw new BusinessException(SubscriptionPlanError.CANNOT_DELETE_DEFAULT_PLAN);
        }

        plan.setStatus(request.getStatus());
        plan.setUpdatedAt(LocalDateTime.now());

        SubscriptionPlanEntity saved = planRepository.save(plan);
        return SubscriptionPlanResponse.fromEntity(saved);
    }
}

