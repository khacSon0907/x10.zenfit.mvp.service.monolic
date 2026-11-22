package x10.zenfit.mvp.service.service.nutritionPlanService.macroUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.INutritionPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionPlanEntity;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.NutritionMacroSummaryResponse;
import x10.zenfit.mvp.service.service.nutritionPlanService.util.MacroCalculator;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.nutrition.NutritionPlanError;

@Component
@RequiredArgsConstructor
public class GetNutritionMacroSummaryUcImpl implements IGetNutritionMacroSummaryUc {

    private final INutritionPlanRepository nutritionPlanRepository;

    @Override
    public NutritionMacroSummaryResponse getMacroSummary(String trainerId, String planId) {
        if (trainerId == null || trainerId.isBlank()) {
            throw new BusinessException(NutritionPlanError.ACCESS_DENIED);
        }

        if (planId == null || planId.isBlank()) {
            throw new BusinessException(NutritionPlanError.PLAN_NOT_FOUND);
        }

        NutritionPlanEntity plan = nutritionPlanRepository.findById(planId.trim())
                .orElseThrow(() -> new BusinessException(NutritionPlanError.PLAN_NOT_FOUND));

        if (!trainerId.trim().equals(plan.getTrainerId())) {
            throw new BusinessException(NutritionPlanError.ACCESS_DENIED);
        }

        return MacroCalculator.calculateSummary(plan.getDailyMacroTarget(), plan.getFoods());
    }
}

