package x10.zenfit.mvp.service.service.trainerTaskService.updateTaskUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.ITrainerTaskRepository;
import x10.zenfit.mvp.service.common.domain.entities.trainer.TrainerTaskEntity;
import x10.zenfit.mvp.service.common.domain.enums.TrainerTaskStatus;
import x10.zenfit.mvp.service.service.trainerTaskService.TrainerTaskResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.trainer.TrainerTaskError;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class UpdateTrainerTaskUcImpl implements IUpdateTrainerTaskUc {

    private final ITrainerTaskRepository trainerTaskRepository;

    @Override
    public TrainerTaskResponse update(String id, UpdateTrainerTaskRequest request, String currentUserId, boolean isManagement) {
        if (id == null || id.isBlank() || request == null) {
            throw new BusinessException(TrainerTaskError.TASK_INVALID_DATA);
        }

        TrainerTaskEntity task = trainerTaskRepository.findById(id.trim())
                .orElseThrow(() -> new BusinessException(TrainerTaskError.TASK_NOT_FOUND));

        if (!isManagement && !currentUserId.equals(task.getTrainerId())) {
            throw new BusinessException(TrainerTaskError.TASK_ACCESS_DENIED);
        }

        if (request.getTitle() != null && !request.getTitle().isBlank()) {
            task.setTitle(request.getTitle().trim());
        }

        if (request.getDescription() != null) {
            task.setDescription(request.getDescription().trim());
        }

        if (request.getColor() != null) {
            task.setColor(request.getColor().trim());
        }

        if (request.getTaskDate() != null) {
            task.setTaskDate(request.getTaskDate());
        }

        if (request.getAllDay() != null) {
            task.setAllDay(request.getAllDay());
        }

        if (request.getStartTime() != null) {
            task.setStartTime(request.getStartTime());
        }

        if (request.getEndTime() != null) {
            task.setEndTime(request.getEndTime());
        }

        if (isManagement && request.getTrainerId() != null && !request.getTrainerId().isBlank()) {
            task.setTrainerId(request.getTrainerId().trim());
        }

        if (!task.isAllDay() && task.getStartTime() != null && task.getEndTime() != null) {
            if (!task.getEndTime().isAfter(task.getStartTime())) {
                throw new BusinessException(TrainerTaskError.TASK_TIME_INVALID);
            }
        }

        if (request.getStatus() != null) {
            if (request.getStatus() == TrainerTaskStatus.DONE && task.getStatus() != TrainerTaskStatus.DONE) {
                task.setCompletedAt(LocalDateTime.now());
            } else if (request.getStatus() != TrainerTaskStatus.DONE) {
                task.setCompletedAt(null);
            }
            task.setStatus(request.getStatus());
        }

        task.setUpdatedAt(LocalDateTime.now());

        TrainerTaskEntity saved = trainerTaskRepository.save(task);
        return TrainerTaskResponse.fromEntity(saved);
    }
}

