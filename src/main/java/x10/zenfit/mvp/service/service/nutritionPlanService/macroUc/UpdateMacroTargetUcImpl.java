package x10.zenfit.mvp.service.service.nutritionPlanService.macroUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import x10.zenfit.mvp.service.Repository.INutritionPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.DailyMacroTarget;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionPlanEntity;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.DailyMacroTargetResponse;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.UpdateMacroTargetRequest;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.nutrition.NutritionPlanError;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateMacroTargetUcImpl implements IUpdateMacroTargetUc {

    private final INutritionPlanRepository nutritionPlanRepository;

    @Override
    @Transactional
    public DailyMacroTargetResponse updateMacroTarget(
            String trainerId,
            String planId,
            UpdateMacroTargetRequest request
    ) {
        if (trainerId == null || trainerId.isBlank()) {
            throw new BusinessException(NutritionPlanError.ACCESS_DENIED);
        }

        if (planId == null || planId.isBlank()) {
            throw new BusinessException(NutritionPlanError.PLAN_NOT_FOUND);
        }

        if (request == null) {
            throw new BusinessException(NutritionPlanError.INVALID_MACRO_TARGET);
        }

        NutritionPlanEntity plan = nutritionPlanRepository.findById(planId.trim())
                .orElseThrow(() -> new BusinessException(NutritionPlanError.PLAN_NOT_FOUND));

        if (!trainerId.trim().equals(plan.getTrainerId())) {
            log.warn("Trainer {} tried to update macro target in plan {} belonging to trainer {}",
                    trainerId, planId, plan.getTrainerId());
            throw new BusinessException(NutritionPlanError.ACCESS_DENIED);
        }

        DailyMacroTarget target = DailyMacroTarget.builder()
                .calories(request.getCalories())
                .protein(request.getProtein())
                .carbs(request.getCarbs())
                .fat(request.getFat())
                .build();

        plan.setDailyMacroTarget(target);
        plan.setUpdatedAt(LocalDateTime.now());
        nutritionPlanRepository.save(plan);
        log.info("Updated macro target for plan {} by trainer {}", planId, trainerId);

        return DailyMacroTargetResponse.builder()
                .calories(target.getCalories())
                .protein(target.getProtein())
                .carbs(target.getCarbs())
                .fat(target.getFat())
                .build();
    }
}

