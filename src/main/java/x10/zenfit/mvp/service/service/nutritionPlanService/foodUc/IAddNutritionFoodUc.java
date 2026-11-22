package x10.zenfit.mvp.service.service.nutritionPlanService.foodUc;

import x10.zenfit.mvp.service.service.nutritionPlanService.dto.NutritionFoodRequest;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.NutritionFoodResponse;

public interface IAddNutritionFoodUc {
    NutritionFoodResponse addFood(String trainerId, String planId, NutritionFoodRequest request);
}

