package x10.zenfit.mvp.service.service.nutritionPlanService.foodUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import x10.zenfit.mvp.service.Repository.INutritionPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionFoodEntity;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionPlanEntity;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.NutritionFoodRequest;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.NutritionFoodResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.nutrition.NutritionPlanError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddNutritionFoodUcImpl implements IAddNutritionFoodUc {

    private final INutritionPlanRepository nutritionPlanRepository;

    @Override
    @Transactional
    public NutritionFoodResponse addFood(String trainerId, String planId, NutritionFoodRequest request) {
        if (trainerId == null || trainerId.isBlank()) {
            throw new BusinessException(NutritionPlanError.ACCESS_DENIED);
        }

        if (planId == null || planId.isBlank()) {
            throw new BusinessException(NutritionPlanError.PLAN_NOT_FOUND);
        }

        if (request == null) {
            throw new BusinessException(NutritionPlanError.INVALID_FOOD_DATA);
        }

        NutritionPlanEntity plan = nutritionPlanRepository.findById(planId.trim())
                .orElseThrow(() -> new BusinessException(NutritionPlanError.PLAN_NOT_FOUND));

        if (!trainerId.trim().equals(plan.getTrainerId())) {
            log.warn("Trainer {} tried to add food to plan {} belonging to trainer {}",
                    trainerId, planId, plan.getTrainerId());
            throw new BusinessException(NutritionPlanError.ACCESS_DENIED);
        }

        if (request.getQuantity() == null || request.getQuantity() <= 0) {
            throw new BusinessException(NutritionPlanError.INVALID_FOOD_DATA);
        }

        NutritionFoodEntity newFood = NutritionFoodEntity.builder()
                .id(UUID.randomUUID().toString())
                .mealType(request.getMealType())
                .foodName(request.getFoodName().trim())
                .quantity(request.getQuantity())
                .unit(request.getUnit() != null ? request.getUnit().trim() : null)
                .calories(request.getCalories() != null ? request.getCalories() : 0.0)
                .protein(request.getProtein() != null ? request.getProtein() : 0.0)
                .carbs(request.getCarbs() != null ? request.getCarbs() : 0.0)
                .fat(request.getFat() != null ? request.getFat() : 0.0)
                .note(request.getNote() != null ? request.getNote().trim() : null)
                .build();

        if (plan.getFoods() == null) {
            plan.setFoods(new ArrayList<>());
        }
        plan.getFoods().add(newFood);
        plan.setUpdatedAt(LocalDateTime.now());

        nutritionPlanRepository.save(plan);
        log.info("Added food {} to plan {} by trainer {}", newFood.getId(), planId, trainerId);

        return NutritionFoodResponse.fromEntity(newFood);
    }
}

