package x10.zenfit.mvp.service.service.trainingSessionService.updateSessionUc;

import x10.zenfit.mvp.service.service.trainingSessionService.TrainingSessionResponse;

public interface IUpdateTrainingSessionUc {
    TrainingSessionResponse updateTrainingSession(String id, UpdateTrainingSessionRequest request, String currentUserId, boolean isManagement);
}

