package x10.zenfit.mvp.service.service.nutritionPlanService.macroUc;

import x10.zenfit.mvp.service.service.nutritionPlanService.dto.NutritionMacroSummaryResponse;

public interface IGetNutritionMacroSummaryUc {
    NutritionMacroSummaryResponse getMacroSummary(String trainerId, String planId);
}

