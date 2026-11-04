package x10.zenfit.mvp.service.service.trainingSessionService.createSessionUc;

import x10.zenfit.mvp.service.service.trainingSessionService.TrainingSessionResponse;

public interface ICreateTrainingSessionUc {
    TrainingSessionResponse createTrainingSession(CreateTrainingSessionRequest request, String currentUserId, boolean isManagement);
}

