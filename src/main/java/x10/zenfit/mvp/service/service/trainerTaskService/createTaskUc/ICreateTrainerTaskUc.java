package x10.zenfit.mvp.service.service.trainerTaskService.createTaskUc;

import x10.zenfit.mvp.service.service.trainerTaskService.TrainerTaskResponse;

public interface ICreateTrainerTaskUc {
    TrainerTaskResponse create(CreateTrainerTaskRequest request, String currentUserId, boolean isManagement);
}

