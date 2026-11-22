package x10.zenfit.mvp.service.service.nutritionPlanService.getPlansUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.Repository.INutritionPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionPlanEntity;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.NutritionPlanResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.nutrition.NutritionPlanError;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetNutritionPlansUcImpl implements IGetNutritionPlansUc {

    private final INutritionPlanRepository nutritionPlanRepository;
    private final IClientRepository clientRepository;

    @Override
    public List<NutritionPlanResponse> getTrainerPlans(String trainerId) {
        if (trainerId == null || trainerId.isBlank()) {
            throw new BusinessException(NutritionPlanError.ACCESS_DENIED);
        }

        List<NutritionPlanEntity> plans = nutritionPlanRepository.findByTrainerId(trainerId.trim());
        if (plans.isEmpty()) {
            return List.of();
        }

        // Lấy danh sách client để hiển thị tên client
        List<ClientEntity> clients = clientRepository.findByAssignedTrainerId(trainerId.trim());
        Map<String, String> clientNameMap = clients.stream()
                .collect(Collectors.toMap(ClientEntity::getId, ClientEntity::getFullName, (existing, replacement) -> existing));

        return plans.stream()
                .map(plan -> NutritionPlanResponse.fromEntity(plan, clientNameMap.getOrDefault(plan.getClientId(), null)))
                .toList();
    }
}

