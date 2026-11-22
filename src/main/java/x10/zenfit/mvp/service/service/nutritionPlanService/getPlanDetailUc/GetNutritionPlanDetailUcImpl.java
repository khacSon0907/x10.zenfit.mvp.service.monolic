package x10.zenfit.mvp.service.service.nutritionPlanService.getPlanDetailUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.Repository.INutritionPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionPlanEntity;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.NutritionPlanResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.nutrition.NutritionPlanError;

@Component
@RequiredArgsConstructor
public class GetNutritionPlanDetailUcImpl implements IGetNutritionPlanDetailUc {

    private final INutritionPlanRepository nutritionPlanRepository;
    private final IClientRepository clientRepository;

    @Override
    public NutritionPlanResponse getPlanDetail(String trainerId, String planId) {
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

        String clientName = clientRepository.findById(plan.getClientId())
                .map(ClientEntity::getFullName)
                .orElse(null);

        return NutritionPlanResponse.fromEntity(plan, clientName);
    }
}

