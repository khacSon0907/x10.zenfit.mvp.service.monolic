package x10.zenfit.mvp.service.service.nutritionPlanService.updatePlanUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.Repository.INutritionPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionPlanEntity;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.NutritionPlanResponse;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.UpdateNutritionPlanRequest;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.nutrition.NutritionPlanError;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateNutritionPlanUcImpl implements IUpdateNutritionPlanUc {

    private final INutritionPlanRepository nutritionPlanRepository;
    private final IClientRepository clientRepository;

    @Override
    @Transactional
    public NutritionPlanResponse updatePlan(String trainerId, String planId, UpdateNutritionPlanRequest request) {
        if (trainerId == null || trainerId.isBlank()) {
            throw new BusinessException(NutritionPlanError.ACCESS_DENIED);
        }

        if (planId == null || planId.isBlank()) {
            throw new BusinessException(NutritionPlanError.PLAN_NOT_FOUND);
        }

        if (request == null) {
            throw new BusinessException(NutritionPlanError.INVALID_DATA);
        }

        NutritionPlanEntity plan = nutritionPlanRepository.findById(planId.trim())
                .orElseThrow(() -> new BusinessException(NutritionPlanError.PLAN_NOT_FOUND));

        if (!trainerId.trim().equals(plan.getTrainerId())) {
            log.warn("Trainer {} tried to update plan {} belonging to trainer {}",
                    trainerId, planId, plan.getTrainerId());
            throw new BusinessException(NutritionPlanError.ACCESS_DENIED);
        }

        LocalDate newStartDate = request.getStartDate() != null ? request.getStartDate() : plan.getStartDate();
        LocalDate newEndDate = request.getEndDate() != null ? request.getEndDate() : plan.getEndDate();

        if (newStartDate != null && newEndDate != null && newStartDate.isAfter(newEndDate)) {
            throw new BusinessException(NutritionPlanError.INVALID_DATE_RANGE);
        }

        if (request.getName() != null && !request.getName().isBlank()) {
            plan.setName(request.getName().trim());
        }

        if (request.getDescription() != null) {
            plan.setDescription(request.getDescription().trim());
        }

        if (request.getStartDate() != null) {
            plan.setStartDate(request.getStartDate());
        }

        if (request.getEndDate() != null) {
            plan.setEndDate(request.getEndDate());
        }

        if (request.getStatus() != null) {
            plan.setStatus(request.getStatus());
        }

        plan.setUpdatedAt(LocalDateTime.now());
        NutritionPlanEntity saved = nutritionPlanRepository.save(plan);

        String clientName = clientRepository.findById(saved.getClientId())
                .map(ClientEntity::getFullName)
                .orElse(null);

        return NutritionPlanResponse.fromEntity(saved, clientName);
    }
}

