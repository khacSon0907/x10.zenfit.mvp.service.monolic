package x10.zenfit.mvp.service.service.nutritionPlanService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.service.nutritionPlanService.createPlanUc.ICreateNutritionPlanUc;
import x10.zenfit.mvp.service.service.nutritionPlanService.deletePlanUc.IDeleteNutritionPlanUc;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.*;
import x10.zenfit.mvp.service.service.nutritionPlanService.foodUc.IAddNutritionFoodUc;
import x10.zenfit.mvp.service.service.nutritionPlanService.foodUc.IDeleteNutritionFoodUc;
import x10.zenfit.mvp.service.service.nutritionPlanService.foodUc.IUpdateNutritionFoodUc;
import x10.zenfit.mvp.service.service.nutritionPlanService.getClientPlansUc.IGetClientNutritionPlansUc;
import x10.zenfit.mvp.service.service.nutritionPlanService.getPlanDetailUc.IGetNutritionPlanDetailUc;
import x10.zenfit.mvp.service.service.nutritionPlanService.getPlansUc.IGetNutritionPlansUc;
import x10.zenfit.mvp.service.service.nutritionPlanService.macroUc.IGetNutritionMacroSummaryUc;
import x10.zenfit.mvp.service.service.nutritionPlanService.macroUc.IUpdateMacroTargetUc;
import x10.zenfit.mvp.service.service.nutritionPlanService.updatePlanUc.IUpdateNutritionPlanUc;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoreNutritionPlanServiceImpl implements ICoreNutritionPlanService {

    private final ICreateNutritionPlanUc createNutritionPlanUc;
    private final IGetNutritionPlansUc getNutritionPlansUc;
    private final IGetNutritionPlanDetailUc getNutritionPlanDetailUc;
    private final IUpdateNutritionPlanUc updateNutritionPlanUc;
    private final IDeleteNutritionPlanUc deleteNutritionPlanUc;
    private final IGetClientNutritionPlansUc getClientNutritionPlansUc;
    private final IAddNutritionFoodUc addNutritionFoodUc;
    private final IUpdateNutritionFoodUc updateNutritionFoodUc;
    private final IDeleteNutritionFoodUc deleteNutritionFoodUc;
    private final IUpdateMacroTargetUc updateMacroTargetUc;
    private final IGetNutritionMacroSummaryUc getNutritionMacroSummaryUc;

    @Override
    public NutritionPlanResponse createPlan(String trainerId, CreateNutritionPlanRequest request) {
        return createNutritionPlanUc.createPlan(trainerId, request);
    }

    @Override
    public List<NutritionPlanResponse> getTrainerPlans(String trainerId) {
        return getNutritionPlansUc.getTrainerPlans(trainerId);
    }

    @Override
    public NutritionPlanResponse getPlanDetail(String trainerId, String planId) {
        return getNutritionPlanDetailUc.getPlanDetail(trainerId, planId);
    }

    @Override
    public NutritionPlanResponse updatePlan(String trainerId, String planId, UpdateNutritionPlanRequest request) {
        return updateNutritionPlanUc.updatePlan(trainerId, planId, request);
    }

    @Override
    public void deletePlan(String trainerId, String planId) {
        deleteNutritionPlanUc.deletePlan(trainerId, planId);
    }

    @Override
    public List<NutritionPlanResponse> getClientPlans(String trainerId, String clientId) {
        return getClientNutritionPlansUc.getClientPlans(trainerId, clientId);
    }

    @Override
    public NutritionFoodResponse addFood(String trainerId, String planId, NutritionFoodRequest request) {
        return addNutritionFoodUc.addFood(trainerId, planId, request);
    }

    @Override
    public NutritionFoodResponse updateFood(String trainerId, String planId, String foodId, UpdateNutritionFoodRequest request) {
        return updateNutritionFoodUc.updateFood(trainerId, planId, foodId, request);
    }

    @Override
    public void deleteFood(String trainerId, String planId, String foodId) {
        deleteNutritionFoodUc.deleteFood(trainerId, planId, foodId);
    }

    @Override
    public DailyMacroTargetResponse updateMacroTarget(String trainerId, String planId, UpdateMacroTargetRequest request) {
        return updateMacroTargetUc.updateMacroTarget(trainerId, planId, request);
    }

    @Override
    public NutritionMacroSummaryResponse getMacroSummary(String trainerId, String planId) {
        return getNutritionMacroSummaryUc.getMacroSummary(trainerId, planId);
    }
}

