package x10.zenfit.mvp.service.service.trainerTaskService.updateTaskUc;

import x10.zenfit.mvp.service.service.trainerTaskService.TrainerTaskResponse;

public interface IUpdateTrainerTaskUc {
    TrainerTaskResponse update(String id, UpdateTrainerTaskRequest request, String currentUserId, boolean isManagement);
}

