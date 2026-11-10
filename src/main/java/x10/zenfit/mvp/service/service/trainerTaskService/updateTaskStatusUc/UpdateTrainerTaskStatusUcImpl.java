package x10.zenfit.mvp.service.service.trainerTaskService.updateTaskStatusUc;

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
public class UpdateTrainerTaskStatusUcImpl implements IUpdateTrainerTaskStatusUc {

    private final ITrainerTaskRepository trainerTaskRepository;

    @Override
    public TrainerTaskResponse updateStatus(String id, UpdateTrainerTaskStatusRequest request, String currentUserId, boolean isManagement) {
        if (id == null || id.isBlank() || request == null || request.getStatus() == null) {
            throw new BusinessException(TrainerTaskError.TASK_INVALID_DATA);
        }

        TrainerTaskEntity task = trainerTaskRepository.findById(id.trim())
                .orElseThrow(() -> new BusinessException(TrainerTaskError.TASK_NOT_FOUND));

        if (!isManagement && !currentUserId.equals(task.getTrainerId())) {
            throw new BusinessException(TrainerTaskError.TASK_ACCESS_DENIED);
        }

        if (request.getStatus() == TrainerTaskStatus.DONE && task.getStatus() != TrainerTaskStatus.DONE) {
            task.setCompletedAt(LocalDateTime.now());
        } else if (request.getStatus() != TrainerTaskStatus.DONE) {
            task.setCompletedAt(null);
        }

        task.setStatus(request.getStatus());
        task.setUpdatedAt(LocalDateTime.now());

        TrainerTaskEntity saved = trainerTaskRepository.save(task);
        return TrainerTaskResponse.fromEntity(saved);
    }
}

