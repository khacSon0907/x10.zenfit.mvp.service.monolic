package x10.zenfit.mvp.service.service.nutritionPlanService.deletePlanUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import x10.zenfit.mvp.service.Repository.INutritionPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionPlanEntity;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.nutrition.NutritionPlanError;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteNutritionPlanUcImpl implements IDeleteNutritionPlanUc {

    private final INutritionPlanRepository nutritionPlanRepository;

    @Override
    @Transactional
    public void deletePlan(String trainerId, String planId) {
        if (trainerId == null || trainerId.isBlank()) {
            throw new BusinessException(NutritionPlanError.ACCESS_DENIED);
        }

        if (planId == null || planId.isBlank()) {
            throw new BusinessException(NutritionPlanError.PLAN_NOT_FOUND);
        }

        NutritionPlanEntity plan = nutritionPlanRepository.findById(planId.trim())
                .orElseThrow(() -> new BusinessException(NutritionPlanError.PLAN_NOT_FOUND));

        if (!trainerId.trim().equals(plan.getTrainerId())) {
            log.warn("Trainer {} tried to delete plan {} belonging to trainer {}",
                    trainerId, planId, plan.getTrainerId());
            throw new BusinessException(NutritionPlanError.ACCESS_DENIED);
        }

        nutritionPlanRepository.deleteById(plan.getId());
        log.info("Deleted nutrition plan {} by trainer {}", planId, trainerId);
    }
}

