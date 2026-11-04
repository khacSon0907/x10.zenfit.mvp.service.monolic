package x10.zenfit.mvp.service.service.trainingSessionService.noShowSessionUc;

import x10.zenfit.mvp.service.service.trainingSessionService.TrainingSessionResponse;

public interface INoShowTrainingSessionUc {
    TrainingSessionResponse noShow(String id, String currentUserId, boolean isManagement);
}

