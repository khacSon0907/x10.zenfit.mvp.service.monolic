package x10.zenfit.mvp.service.service.trainerTaskService.createTaskUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.ITrainerTaskRepository;
import x10.zenfit.mvp.service.common.domain.entities.trainer.TrainerTaskEntity;
import x10.zenfit.mvp.service.common.domain.enums.TrainerTaskStatus;
import x10.zenfit.mvp.service.service.trainerTaskService.TrainerTaskResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.trainer.TrainerTaskError;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CreateTrainerTaskUcImpl implements ICreateTrainerTaskUc {

    private final ITrainerTaskRepository trainerTaskRepository;

    @Override
    public TrainerTaskResponse create(CreateTrainerTaskRequest request, String currentUserId, boolean isManagement) {
        if (request == null) {
            throw new BusinessException(TrainerTaskError.TASK_INVALID_DATA);
        }

        String trainerId;
        if (isManagement && request.getTrainerId() != null && !request.getTrainerId().isBlank()) {
            trainerId = request.getTrainerId().trim();
        } else {
            trainerId = currentUserId;
        }

        if (!request.isAllDay() && request.getStartTime() != null && request.getEndTime() != null) {
            if (!request.getEndTime().isAfter(request.getStartTime())) {
                throw new BusinessException(TrainerTaskError.TASK_TIME_INVALID);
            }
        }

        LocalDateTime now = LocalDateTime.now();

        TrainerTaskEntity entity = TrainerTaskEntity.builder()
                .trainerId(trainerId)
                .title(request.getTitle().trim())
                .description(request.getDescription() != null ? request.getDescription().trim() : null)
                .color(request.getColor() != null && !request.getColor().isBlank() ? request.getColor().trim() : null)
                .taskDate(request.getTaskDate())
                .startTime(request.getStartTime())
                .endTime(request.getEndTime())
                .allDay(request.isAllDay())
                .status(TrainerTaskStatus.TODO)
                .completedAt(null)
                .createdAt(now)
                .updatedAt(now)
                .build();

        TrainerTaskEntity saved = trainerTaskRepository.save(entity);
        return TrainerTaskResponse.fromEntity(saved);
    }
}

