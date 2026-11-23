package x10.zenfit.mvp.service.service.workoutPlanService.getClientPlansUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.Repository.IWorkoutPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutPlanEntity;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutPlanResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.workout.WorkoutPlanError;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetClientWorkoutPlansUcImpl implements IGetClientWorkoutPlansUc {

    private final IWorkoutPlanRepository workoutPlanRepository;
    private final IClientRepository clientRepository;

    @Override
    public List<WorkoutPlanResponse> getClientPlans(String trainerId, String clientId) {
        if (trainerId == null || trainerId.isBlank()) {
            throw new BusinessException(WorkoutPlanError.ACCESS_DENIED);
        }

        if (clientId == null || clientId.isBlank()) {
            throw new BusinessException(WorkoutPlanError.CLIENT_NOT_FOUND);
        }

        // 1. Kiểm tra Client tồn tại
        ClientEntity client = clientRepository.findById(clientId.trim())
                .orElseThrow(() -> new BusinessException(WorkoutPlanError.CLIENT_NOT_FOUND));

        // 2. Kiểm tra quyền sở hữu Client của PT
        if (client.getAssignedTrainerId() == null || !client.getAssignedTrainerId().equals(trainerId.trim())) {
            log.warn("Trainer {} tried to view workout plans of client {} assigned to {}",
                    trainerId, clientId, client.getAssignedTrainerId());
            throw new BusinessException(WorkoutPlanError.CLIENT_NOT_ASSIGNED_TO_TRAINER);
        }

        List<WorkoutPlanEntity> plans = workoutPlanRepository.findByTrainerIdAndClientId(trainerId.trim(), clientId.trim());

        return plans.stream()
                .map(plan -> WorkoutPlanResponse.fromEntity(plan, client.getFullName()))
                .toList();
    }
}

