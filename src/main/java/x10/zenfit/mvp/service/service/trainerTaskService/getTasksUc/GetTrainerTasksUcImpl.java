package x10.zenfit.mvp.service.service.trainerTaskService.getTasksUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.ITrainerTaskRepository;
import x10.zenfit.mvp.service.common.domain.entities.trainer.TrainerTaskEntity;
import x10.zenfit.mvp.service.service.trainerTaskService.TrainerTaskResponse;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetTrainerTasksUcImpl implements IGetTrainerTasksUc {

    private final ITrainerTaskRepository trainerTaskRepository;

    @Override
    public List<TrainerTaskResponse> getTasks(TrainerTaskFilterQuery query, String currentUserId, boolean isManagement) {
        String targetTrainerId;
        if (!isManagement) {
            targetTrainerId = currentUserId;
        } else {
            targetTrainerId = (query != null && query.getTrainerId() != null && !query.getTrainerId().isBlank())
                    ? query.getTrainerId().trim()
                    : null;
        }

        LocalDate taskDate = query != null ? query.getTaskDate() : null;
        LocalDate startDate = query != null ? query.getStartDate() : null;
        LocalDate endDate = query != null ? query.getEndDate() : null;
        var status = query != null ? query.getStatus() : null;

        List<TrainerTaskEntity> entities = trainerTaskRepository.findTasks(
                targetTrainerId,
                taskDate,
                startDate,
                endDate,
                status
        );

        return entities.stream()
                .map(TrainerTaskResponse::fromEntity)
                .toList();
    }
}

