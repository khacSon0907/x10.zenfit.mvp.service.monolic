package x10.zenfit.mvp.service.service.workoutPlanService.getPlanDetailUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.Repository.IWorkoutPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutPlanEntity;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutPlanResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.workout.WorkoutPlanError;

@Component
@RequiredArgsConstructor
public class GetWorkoutPlanDetailUcImpl implements IGetWorkoutPlanDetailUc {

    private final IWorkoutPlanRepository workoutPlanRepository;
    private final IClientRepository clientRepository;

    @Override
    public WorkoutPlanResponse getPlanDetail(String trainerId, String planId) {
        if (trainerId == null || trainerId.isBlank()) {
            throw new BusinessException(WorkoutPlanError.ACCESS_DENIED);
        }

        if (planId == null || planId.isBlank()) {
            throw new BusinessException(WorkoutPlanError.PLAN_NOT_FOUND);
        }

        WorkoutPlanEntity plan = workoutPlanRepository.findById(planId.trim())
                .orElseThrow(() -> new BusinessException(WorkoutPlanError.PLAN_NOT_FOUND));

        if (!trainerId.trim().equals(plan.getTrainerId())) {
            throw new BusinessException(WorkoutPlanError.ACCESS_DENIED);
        }

        String clientName = clientRepository.findById(plan.getClientId())
                .map(ClientEntity::getFullName)
                .orElse(null);

        return WorkoutPlanResponse.fromEntity(plan, clientName);
    }
}

