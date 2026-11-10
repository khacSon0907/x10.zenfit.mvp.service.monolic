package x10.zenfit.mvp.service.service.trainerTaskService;

import x10.zenfit.mvp.service.service.trainerTaskService.createTaskUc.CreateTrainerTaskRequest;
import x10.zenfit.mvp.service.service.trainerTaskService.getTasksUc.TrainerTaskFilterQuery;
import x10.zenfit.mvp.service.service.trainerTaskService.updateTaskStatusUc.UpdateTrainerTaskStatusRequest;
import x10.zenfit.mvp.service.service.trainerTaskService.updateTaskUc.UpdateTrainerTaskRequest;

import java.util.List;

public interface ICoreTrainerTaskService {

    TrainerTaskResponse create(CreateTrainerTaskRequest request, String currentUserId, boolean isManagement);

    TrainerTaskResponse update(String id, UpdateTrainerTaskRequest request, String currentUserId, boolean isManagement);

    TrainerTaskResponse updateStatus(String id, UpdateTrainerTaskStatusRequest request, String currentUserId, boolean isManagement);

    TrainerTaskResponse getById(String id, String currentUserId, boolean isManagement);

    List<TrainerTaskResponse> getTasks(TrainerTaskFilterQuery query, String currentUserId, boolean isManagement);

    void delete(String id, String currentUserId, boolean isManagement);
}

