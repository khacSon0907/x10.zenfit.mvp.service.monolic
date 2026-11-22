package x10.zenfit.mvp.service.service.nutritionPlanService;

import x10.zenfit.mvp.service.service.nutritionPlanService.dto.*;

import java.util.List;

public interface ICoreNutritionPlanService {

    NutritionPlanResponse createPlan(String trainerId, CreateNutritionPlanRequest request);

    List<NutritionPlanResponse> getTrainerPlans(String trainerId);

    NutritionPlanResponse getPlanDetail(String trainerId, String planId);

    NutritionPlanResponse updatePlan(String trainerId, String planId, UpdateNutritionPlanRequest request);

    void deletePlan(String trainerId, String planId);

    List<NutritionPlanResponse> getClientPlans(String trainerId, String clientId);

    NutritionFoodResponse addFood(String trainerId, String planId, NutritionFoodRequest request);

    NutritionFoodResponse updateFood(String trainerId, String planId, String foodId, UpdateNutritionFoodRequest request);

    void deleteFood(String trainerId, String planId, String foodId);

    DailyMacroTargetResponse updateMacroTarget(String trainerId, String planId, UpdateMacroTargetRequest request);

    NutritionMacroSummaryResponse getMacroSummary(String trainerId, String planId);
}

