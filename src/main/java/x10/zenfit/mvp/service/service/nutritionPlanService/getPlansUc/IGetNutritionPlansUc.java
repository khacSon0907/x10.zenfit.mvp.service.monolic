package x10.zenfit.mvp.service.service.nutritionPlanService.getPlansUc;

import x10.zenfit.mvp.service.service.nutritionPlanService.dto.NutritionPlanResponse;

import java.util.List;

public interface IGetNutritionPlansUc {
    List<NutritionPlanResponse> getTrainerPlans(String trainerId);
}

