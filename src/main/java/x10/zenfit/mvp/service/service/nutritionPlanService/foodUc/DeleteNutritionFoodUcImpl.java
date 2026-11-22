package x10.zenfit.mvp.service.service.nutritionPlanService.foodUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import x10.zenfit.mvp.service.Repository.INutritionPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionFoodEntity;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionPlanEntity;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.nutrition.NutritionPlanError;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteNutritionFoodUcImpl implements IDeleteNutritionFoodUc {

    private final INutritionPlanRepository nutritionPlanRepository;

    @Override
    @Transactional
    public void deleteFood(String trainerId, String planId, String foodId) {
        if (trainerId == null || trainerId.isBlank()) {
            throw new BusinessException(NutritionPlanError.ACCESS_DENIED);
        }

        if (planId == null || planId.isBlank()) {
            throw new BusinessException(NutritionPlanError.PLAN_NOT_FOUND);
        }

        if (foodId == null || foodId.isBlank()) {
            throw new BusinessException(NutritionPlanError.FOOD_NOT_FOUND);
        }

        NutritionPlanEntity plan = nutritionPlanRepository.findById(planId.trim())
                .orElseThrow(() -> new BusinessException(NutritionPlanError.PLAN_NOT_FOUND));

        if (!trainerId.trim().equals(plan.getTrainerId())) {
            log.warn("Trainer {} tried to delete food {} in plan {} belonging to trainer {}",
                    trainerId, foodId, planId, plan.getTrainerId());
            throw new BusinessException(NutritionPlanError.ACCESS_DENIED);
        }

        if (plan.getFoods() == null || plan.getFoods().isEmpty()) {
            throw new BusinessException(NutritionPlanError.FOOD_NOT_FOUND);
        }

        boolean removed = plan.getFoods().removeIf(f -> foodId.trim().equals(f.getId()));
        if (!removed) {
            throw new BusinessException(NutritionPlanError.FOOD_NOT_FOUND);
        }

        plan.setUpdatedAt(LocalDateTime.now());
        nutritionPlanRepository.save(plan);
        log.info("Deleted food {} from plan {} by trainer {}", foodId, planId, trainerId);
    }
}

