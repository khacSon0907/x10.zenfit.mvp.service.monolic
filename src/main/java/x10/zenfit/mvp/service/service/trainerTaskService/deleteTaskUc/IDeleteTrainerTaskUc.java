package x10.zenfit.mvp.service.service.trainerTaskService.deleteTaskUc;

public interface IDeleteTrainerTaskUc {
    void delete(String id, String currentUserId, boolean isManagement);
}

