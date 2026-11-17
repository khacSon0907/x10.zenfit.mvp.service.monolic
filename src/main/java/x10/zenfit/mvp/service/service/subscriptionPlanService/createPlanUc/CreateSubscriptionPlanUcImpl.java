package x10.zenfit.mvp.service.service.subscriptionPlanService.createPlanUc;

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
public class CreateSubscriptionPlanUcImpl implements ICreateSubscriptionPlanUc {

    private final ISubscriptionPlanRepository planRepository;

    @Override
    @Transactional
    public SubscriptionPlanResponse execute(CreateSubscriptionPlanRequest request) {
        if (request == null) {
            throw new BusinessException(SubscriptionPlanError.INVALID_PLAN_DATA);
        }

        String normalizedCode = request.getCode().trim().toUpperCase();

        if (planRepository.existsByCode(normalizedCode)) {
            throw new BusinessException(SubscriptionPlanError.CODE_ALREADY_EXISTS);
        }

        boolean isDefault = Boolean.TRUE.equals(request.getIsDefault());
        SubscriptionPlanStatus status = request.getStatus() != null
                ? request.getStatus()
                : SubscriptionPlanStatus.ACTIVE;
        int displayOrder = request.getDisplayOrder() != null ? request.getDisplayOrder() : 0;
        int maxTrainingSessions = request.getMaxTrainingSessions() != null ? request.getMaxTrainingSessions() : 0;

        LocalDateTime now = LocalDateTime.now();

        SubscriptionPlanEntity newPlan = SubscriptionPlanEntity.builder()
                .code(normalizedCode)
                .name(request.getName().trim())
                .description(request.getDescription())
                .price(request.getPrice())
                .durationDays(request.getDurationDays())
                .maxStudents(request.getMaxStudents())
                .maxTrainingSessions(maxTrainingSessions)
                .status(status)
                .isDefault(isDefault)
                .displayOrder(displayOrder)
                .createdAt(now)
                .updatedAt(now)
                .build();

        SubscriptionPlanEntity saved = planRepository.save(newPlan);

        if (isDefault && saved.getId() != null) {
            planRepository.clearDefaultFlagExcept(saved.getId());
        }

        return SubscriptionPlanResponse.fromEntity(saved);
    }
}

