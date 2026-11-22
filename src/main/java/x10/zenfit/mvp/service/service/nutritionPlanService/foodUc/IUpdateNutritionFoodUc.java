package x10.zenfit.mvp.service.service.nutritionPlanService.foodUc;

import x10.zenfit.mvp.service.service.nutritionPlanService.dto.NutritionFoodResponse;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.UpdateNutritionFoodRequest;

public interface IUpdateNutritionFoodUc {
    NutritionFoodResponse updateFood(String trainerId, String planId, String foodId, UpdateNutritionFoodRequest request);
}

