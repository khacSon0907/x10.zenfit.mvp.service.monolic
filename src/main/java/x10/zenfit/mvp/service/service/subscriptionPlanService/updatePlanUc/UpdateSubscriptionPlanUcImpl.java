package x10.zenfit.mvp.service.service.subscriptionPlanService.updatePlanUc;

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
public class UpdateSubscriptionPlanUcImpl implements IUpdateSubscriptionPlanUc {

    private final ISubscriptionPlanRepository planRepository;

    @Override
    @Transactional
    public SubscriptionPlanResponse execute(String id, UpdateSubscriptionPlanRequest request) {
        if (id == null || id.isBlank()) {
            throw new BusinessException(SubscriptionPlanError.PLAN_NOT_FOUND);
        }

        if (request == null) {
            throw new BusinessException(SubscriptionPlanError.INVALID_PLAN_DATA);
        }

        SubscriptionPlanEntity plan = planRepository.findById(id.trim())
                .orElseThrow(() -> new BusinessException(SubscriptionPlanError.PLAN_NOT_FOUND));

        if (plan.getStatus() == SubscriptionPlanStatus.ARCHIVED) {
            throw new BusinessException(SubscriptionPlanError.CANNOT_MODIFY_ARCHIVED_PLAN);
        }

        plan.setName(request.getName().trim());
        plan.setDescription(request.getDescription());
        plan.setPrice(request.getPrice());
        plan.setDurationDays(request.getDurationDays());
        plan.setMaxStudents(request.getMaxStudents());

        if (request.getMaxTrainingSessions() != null) {
            plan.setMaxTrainingSessions(request.getMaxTrainingSessions());
        }

        if (request.getDisplayOrder() != null) {
            plan.setDisplayOrder(request.getDisplayOrder());
        }

        if (request.getIsDefault() != null) {
            if (Boolean.TRUE.equals(request.getIsDefault())) {
                plan.setIsDefault(true);
                planRepository.clearDefaultFlagExcept(plan.getId());
            } else if (Boolean.TRUE.equals(plan.getIsDefault())) {
                // Không cho phép tự ý bỏ cờ default trực tiếp mà không chỉ định gói khác làm default
                throw new BusinessException(SubscriptionPlanError.CANNOT_DELETE_DEFAULT_PLAN);
            }
        }

        plan.setUpdatedAt(LocalDateTime.now());

        SubscriptionPlanEntity saved = planRepository.save(plan);
        return SubscriptionPlanResponse.fromEntity(saved);
    }
}

