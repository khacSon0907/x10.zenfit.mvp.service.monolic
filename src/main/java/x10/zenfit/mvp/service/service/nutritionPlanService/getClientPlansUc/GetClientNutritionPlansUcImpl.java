package x10.zenfit.mvp.service.service.nutritionPlanService.getClientPlansUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.Repository.INutritionPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionPlanEntity;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.NutritionPlanResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.nutrition.NutritionPlanError;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetClientNutritionPlansUcImpl implements IGetClientNutritionPlansUc {

    private final INutritionPlanRepository nutritionPlanRepository;
    private final IClientRepository clientRepository;

    @Override
    public List<NutritionPlanResponse> getClientPlans(String trainerId, String clientId) {
        if (trainerId == null || trainerId.isBlank()) {
            throw new BusinessException(NutritionPlanError.ACCESS_DENIED);
        }

        if (clientId == null || clientId.isBlank()) {
            throw new BusinessException(NutritionPlanError.CLIENT_NOT_FOUND);
        }

        // 1. Kiểm tra Client tồn tại
        ClientEntity client = clientRepository.findById(clientId.trim())
                .orElseThrow(() -> new BusinessException(NutritionPlanError.CLIENT_NOT_FOUND));

        // 2. Kiểm tra quyền sở hữu Client của PT
        if (client.getAssignedTrainerId() == null || !client.getAssignedTrainerId().equals(trainerId.trim())) {
            log.warn("Trainer {} tried to view plans of client {} assigned to {}",
                    trainerId, clientId, client.getAssignedTrainerId());
            throw new BusinessException(NutritionPlanError.CLIENT_NOT_ASSIGNED_TO_TRAINER);
        }

        List<NutritionPlanEntity> plans = nutritionPlanRepository.findByTrainerIdAndClientId(trainerId.trim(), clientId.trim());

        return plans.stream()
                .map(plan -> NutritionPlanResponse.fromEntity(plan, client.getFullName()))
                .toList();
    }
}

