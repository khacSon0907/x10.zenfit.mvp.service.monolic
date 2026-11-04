package x10.zenfit.mvp.service.service.trainingSessionService.getSessionsUc;

public interface IGetTrainingSessionsUc {
    PaginatedTrainingSessionResponse getTrainingSessions(TrainingSessionFilterQuery query, String currentUserId, boolean isManagement);
}

