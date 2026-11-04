package x10.zenfit.mvp.service.service.trainingSessionService.cancelSessionUc;

import x10.zenfit.mvp.service.service.trainingSessionService.TrainingSessionResponse;

public interface ICancelTrainingSessionUc {
    TrainingSessionResponse cancel(String id, String currentUserId, boolean isManagement);
}

