package x10.zenfit.mvp.service.service.trainerTaskService.updateTaskStatusUc;

import x10.zenfit.mvp.service.service.trainerTaskService.TrainerTaskResponse;

public interface IUpdateTrainerTaskStatusUc {
    TrainerTaskResponse updateStatus(String id, UpdateTrainerTaskStatusRequest request, String currentUserId, boolean isManagement);
}

