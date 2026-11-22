package x10.zenfit.mvp.service.service.nutritionPlanService.foodUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import x10.zenfit.mvp.service.Repository.INutritionPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionFoodEntity;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionPlanEntity;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.NutritionFoodResponse;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.UpdateNutritionFoodRequest;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.nutrition.NutritionPlanError;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateNutritionFoodUcImpl implements IUpdateNutritionFoodUc {

    private final INutritionPlanRepository nutritionPlanRepository;

    @Override
    @Transactional
    public NutritionFoodResponse updateFood(
            String trainerId,
            String planId,
            String foodId,
            UpdateNutritionFoodRequest request
    ) {
        if (trainerId == null || trainerId.isBlank()) {
            throw new BusinessException(NutritionPlanError.ACCESS_DENIED);
        }

        if (planId == null || planId.isBlank()) {
            throw new BusinessException(NutritionPlanError.PLAN_NOT_FOUND);
        }

        if (foodId == null || foodId.isBlank()) {
            throw new BusinessException(NutritionPlanError.FOOD_NOT_FOUND);
        }

        if (request == null) {
            throw new BusinessException(NutritionPlanError.INVALID_FOOD_DATA);
        }

        NutritionPlanEntity plan = nutritionPlanRepository.findById(planId.trim())
                .orElseThrow(() -> new BusinessException(NutritionPlanError.PLAN_NOT_FOUND));

        if (!trainerId.trim().equals(plan.getTrainerId())) {
            log.warn("Trainer {} tried to update food {} in plan {} belonging to trainer {}",
                    trainerId, foodId, planId, plan.getTrainerId());
            throw new BusinessException(NutritionPlanError.ACCESS_DENIED);
        }

        if (plan.getFoods() == null || plan.getFoods().isEmpty()) {
            throw new BusinessException(NutritionPlanError.FOOD_NOT_FOUND);
        }

        NutritionFoodEntity targetFood = plan.getFoods().stream()
                .filter(f -> foodId.trim().equals(f.getId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(NutritionPlanError.FOOD_NOT_FOUND));

        if (request.getMealType() != null) {
            targetFood.setMealType(request.getMealType());
        }

        if (request.getFoodName() != null && !request.getFoodName().isBlank()) {
            targetFood.setFoodName(request.getFoodName().trim());
        }

        if (request.getQuantity() != null) {
            if (request.getQuantity() <= 0) {
                throw new BusinessException(NutritionPlanError.INVALID_FOOD_DATA);
            }
            targetFood.setQuantity(request.getQuantity());
        }

        if (request.getUnit() != null) {
            targetFood.setUnit(request.getUnit().trim());
        }

        if (request.getCalories() != null) {
            targetFood.setCalories(request.getCalories());
        }

        if (request.getProtein() != null) {
            targetFood.setProtein(request.getProtein());
        }

        if (request.getCarbs() != null) {
            targetFood.setCarbs(request.getCarbs());
        }

        if (request.getFat() != null) {
            targetFood.setFat(request.getFat());
        }

        if (request.getNote() != null) {
            targetFood.setNote(request.getNote().trim());
        }

        plan.setUpdatedAt(LocalDateTime.now());
        nutritionPlanRepository.save(plan);
        log.info("Updated food {} in plan {} by trainer {}", foodId, planId, trainerId);

        return NutritionFoodResponse.fromEntity(targetFood);
    }
}

