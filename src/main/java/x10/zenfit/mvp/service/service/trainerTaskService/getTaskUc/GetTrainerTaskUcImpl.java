package x10.zenfit.mvp.service.service.trainerTaskService.getTaskUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.ITrainerTaskRepository;
import x10.zenfit.mvp.service.common.domain.entities.trainer.TrainerTaskEntity;
import x10.zenfit.mvp.service.service.trainerTaskService.TrainerTaskResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.trainer.TrainerTaskError;

@Component
@RequiredArgsConstructor
public class GetTrainerTaskUcImpl implements IGetTrainerTaskUc {

    private final ITrainerTaskRepository trainerTaskRepository;

    @Override
    public TrainerTaskResponse getById(String id, String currentUserId, boolean isManagement) {
        if (id == null || id.isBlank()) {
            throw new BusinessException(TrainerTaskError.TASK_INVALID_DATA);
        }

        TrainerTaskEntity task = trainerTaskRepository.findById(id.trim())
                .orElseThrow(() -> new BusinessException(TrainerTaskError.TASK_NOT_FOUND));

        if (!isManagement && !currentUserId.equals(task.getTrainerId())) {
            throw new BusinessException(TrainerTaskError.TASK_ACCESS_DENIED);
        }

        return TrainerTaskResponse.fromEntity(task);
    }
}

