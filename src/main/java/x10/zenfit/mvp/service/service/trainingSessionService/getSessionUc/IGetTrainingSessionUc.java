package x10.zenfit.mvp.service.service.trainingSessionService.getSessionUc;

import x10.zenfit.mvp.service.service.trainingSessionService.TrainingSessionResponse;

public interface IGetTrainingSessionUc {
    TrainingSessionResponse getTrainingSessionById(String id, String currentUserId, boolean isManagement);
}

