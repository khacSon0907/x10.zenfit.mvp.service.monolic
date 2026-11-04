package x10.zenfit.mvp.service.service.trainingSessionService.completeSessionUc;

import x10.zenfit.mvp.service.service.trainingSessionService.TrainingSessionResponse;

public interface ICompleteTrainingSessionUc {

    TrainingSessionResponse complete(String id, CompleteTrainingSessionRequest request, String currentUserId, boolean isManagement);

    default TrainingSessionResponse complete(String id, String currentUserId, boolean isManagement) {
        return complete(id, null, currentUserId, isManagement);
    }
}
