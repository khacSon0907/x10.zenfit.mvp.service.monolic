package x10.zenfit.mvp.service.service.nutritionPlanService.createPlanUc;

import x10.zenfit.mvp.service.service.nutritionPlanService.dto.CreateNutritionPlanRequest;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.NutritionPlanResponse;

public interface ICreateNutritionPlanUc {
    NutritionPlanResponse createPlan(String trainerId, CreateNutritionPlanRequest request);
}

