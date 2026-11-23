package x10.zenfit.mvp.service.service.workoutPlanService.updatePlanUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.Repository.IWorkoutPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutPlanEntity;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.UpdateWorkoutPlanRequest;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutPlanResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.workout.WorkoutPlanError;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateWorkoutPlanUcImpl implements IUpdateWorkoutPlanUc {

    private final IWorkoutPlanRepository workoutPlanRepository;
    private final IClientRepository clientRepository;

    @Override
    @Transactional
    public WorkoutPlanResponse updatePlan(String trainerId, String planId, UpdateWorkoutPlanRequest request) {
        if (trainerId == null || trainerId.isBlank()) {
            throw new BusinessException(WorkoutPlanError.ACCESS_DENIED);
        }

        if (planId == null || planId.isBlank()) {
            throw new BusinessException(WorkoutPlanError.PLAN_NOT_FOUND);
        }

        if (request == null) {
            throw new BusinessException(WorkoutPlanError.INVALID_DATA);
        }

        WorkoutPlanEntity plan = workoutPlanRepository.findById(planId.trim())
                .orElseThrow(() -> new BusinessException(WorkoutPlanError.PLAN_NOT_FOUND));

        if (!trainerId.trim().equals(plan.getTrainerId())) {
            log.warn("Trainer {} tried to update plan {} belonging to trainer {}",
                    trainerId, planId, plan.getTrainerId());
            throw new BusinessException(WorkoutPlanError.ACCESS_DENIED);
        }

        LocalDate newStartDate = request.getStartDate() != null ? request.getStartDate() : plan.getStartDate();
        LocalDate newEndDate = request.getEndDate() != null ? request.getEndDate() : plan.getEndDate();

        if (newStartDate != null && newEndDate != null && newStartDate.isAfter(newEndDate)) {
            throw new BusinessException(WorkoutPlanError.INVALID_DATE_RANGE);
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
        WorkoutPlanEntity saved = workoutPlanRepository.save(plan);

        String clientName = clientRepository.findById(saved.getClientId())
                .map(ClientEntity::getFullName)
                .orElse(null);

        return WorkoutPlanResponse.fromEntity(saved, clientName);
    }
}

