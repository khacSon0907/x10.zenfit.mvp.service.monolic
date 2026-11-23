package x10.zenfit.mvp.service.service.workoutPlanService.getPlansUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.Repository.IWorkoutPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutPlanEntity;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutPlanResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.workout.WorkoutPlanError;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class GetWorkoutPlansUcImpl implements IGetWorkoutPlansUc {

    private final IWorkoutPlanRepository workoutPlanRepository;
    private final IClientRepository clientRepository;

    @Override
    public List<WorkoutPlanResponse> getTrainerPlans(String trainerId) {
        if (trainerId == null || trainerId.isBlank()) {
            throw new BusinessException(WorkoutPlanError.ACCESS_DENIED);
        }

        List<WorkoutPlanEntity> plans = workoutPlanRepository.findByTrainerId(trainerId.trim());
        if (plans.isEmpty()) {
            return List.of();
        }

        // Lấy danh sách client để hiển thị tên client tương ứng
        List<ClientEntity> clients = clientRepository.findByAssignedTrainerId(trainerId.trim());
        Map<String, String> clientNameMap = clients.stream()
                .collect(Collectors.toMap(ClientEntity::getId, ClientEntity::getFullName, (existing, replacement) -> existing));

        return plans.stream()
                .map(plan -> WorkoutPlanResponse.fromEntity(plan, clientNameMap.getOrDefault(plan.getClientId(), null)))
                .toList();
    }
}

