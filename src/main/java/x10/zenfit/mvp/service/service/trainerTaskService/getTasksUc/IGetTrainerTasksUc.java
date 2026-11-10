package x10.zenfit.mvp.service.service.trainerTaskService.getTasksUc;

import x10.zenfit.mvp.service.service.trainerTaskService.TrainerTaskResponse;

import java.util.List;

public interface IGetTrainerTasksUc {
    List<TrainerTaskResponse> getTasks(TrainerTaskFilterQuery query, String currentUserId, boolean isManagement);
}

