package x10.zenfit.mvp.service.service.nutritionPlanService.updatePlanUc;

import x10.zenfit.mvp.service.service.nutritionPlanService.dto.NutritionPlanResponse;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.UpdateNutritionPlanRequest;

public interface IUpdateNutritionPlanUc {
    NutritionPlanResponse updatePlan(String trainerId, String planId, UpdateNutritionPlanRequest request);
}

