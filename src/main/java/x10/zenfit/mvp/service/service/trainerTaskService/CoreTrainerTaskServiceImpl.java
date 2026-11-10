package x10.zenfit.mvp.service.service.trainerTaskService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.service.trainerTaskService.createTaskUc.CreateTrainerTaskRequest;
import x10.zenfit.mvp.service.service.trainerTaskService.createTaskUc.ICreateTrainerTaskUc;
import x10.zenfit.mvp.service.service.trainerTaskService.deleteTaskUc.IDeleteTrainerTaskUc;
import x10.zenfit.mvp.service.service.trainerTaskService.getTaskUc.IGetTrainerTaskUc;
import x10.zenfit.mvp.service.service.trainerTaskService.getTasksUc.IGetTrainerTasksUc;
import x10.zenfit.mvp.service.service.trainerTaskService.getTasksUc.TrainerTaskFilterQuery;
import x10.zenfit.mvp.service.service.trainerTaskService.updateTaskStatusUc.IUpdateTrainerTaskStatusUc;
import x10.zenfit.mvp.service.service.trainerTaskService.updateTaskStatusUc.UpdateTrainerTaskStatusRequest;
import x10.zenfit.mvp.service.service.trainerTaskService.updateTaskUc.IUpdateTrainerTaskUc;
import x10.zenfit.mvp.service.service.trainerTaskService.updateTaskUc.UpdateTrainerTaskRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoreTrainerTaskServiceImpl implements ICoreTrainerTaskService {

    private final ICreateTrainerTaskUc createTrainerTaskUc;
    private final IUpdateTrainerTaskUc updateTrainerTaskUc;
    private final IUpdateTrainerTaskStatusUc updateTrainerTaskStatusUc;
    private final IGetTrainerTaskUc getTrainerTaskUc;
    private final IGetTrainerTasksUc getTrainerTasksUc;
    private final IDeleteTrainerTaskUc deleteTrainerTaskUc;

    @Override
    public TrainerTaskResponse create(CreateTrainerTaskRequest request, String currentUserId, boolean isManagement) {
        return createTrainerTaskUc.create(request, currentUserId, isManagement);
    }

    @Override
    public TrainerTaskResponse update(String id, UpdateTrainerTaskRequest request, String currentUserId, boolean isManagement) {
        return updateTrainerTaskUc.update(id, request, currentUserId, isManagement);
    }

    @Override
    public TrainerTaskResponse updateStatus(String id, UpdateTrainerTaskStatusRequest request, String currentUserId, boolean isManagement) {
        return updateTrainerTaskStatusUc.updateStatus(id, request, currentUserId, isManagement);
    }

    @Override
    public TrainerTaskResponse getById(String id, String currentUserId, boolean isManagement) {
        return getTrainerTaskUc.getById(id, currentUserId, isManagement);
    }

    @Override
    public List<TrainerTaskResponse> getTasks(TrainerTaskFilterQuery query, String currentUserId, boolean isManagement) {
        return getTrainerTasksUc.getTasks(query, currentUserId, isManagement);
    }

    @Override
    public void delete(String id, String currentUserId, boolean isManagement) {
        deleteTrainerTaskUc.delete(id, currentUserId, isManagement);
    }
}

