package x10.zenfit.mvp.service.service.nutritionPlanService.macroUc;

import x10.zenfit.mvp.service.service.nutritionPlanService.dto.DailyMacroTargetResponse;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.UpdateMacroTargetRequest;

public interface IUpdateMacroTargetUc {
    DailyMacroTargetResponse updateMacroTarget(String trainerId, String planId, UpdateMacroTargetRequest request);
}

