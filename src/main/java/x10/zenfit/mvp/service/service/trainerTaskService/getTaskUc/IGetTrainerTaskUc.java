package x10.zenfit.mvp.service.service.trainerTaskService.getTaskUc;

import x10.zenfit.mvp.service.service.trainerTaskService.TrainerTaskResponse;

public interface IGetTrainerTaskUc {
    TrainerTaskResponse getById(String id, String currentUserId, boolean isManagement);
}

