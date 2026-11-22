package x10.zenfit.mvp.service.service.nutritionPlanService.getClientPlansUc;

import x10.zenfit.mvp.service.service.nutritionPlanService.dto.NutritionPlanResponse;

import java.util.List;

public interface IGetClientNutritionPlansUc {
    List<NutritionPlanResponse> getClientPlans(String trainerId, String clientId);
}

