package x10.zenfit.mvp.service.service.nutritionPlanService.getPlanDetailUc;

import x10.zenfit.mvp.service.service.nutritionPlanService.dto.NutritionPlanResponse;

public interface IGetNutritionPlanDetailUc {
    NutritionPlanResponse getPlanDetail(String trainerId, String planId);
}

