package x10.zenfit.mvp.service.service.trainingSessionService.getSessionUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.ITrainingSessionRepository;
import x10.zenfit.mvp.service.common.domain.entities.session.TrainingSessionEntity;
import x10.zenfit.mvp.service.service.trainingSessionService.TrainingSessionResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.session.TrainingSessionError;

@Component
@RequiredArgsConstructor
public class GetTrainingSessionUcImpl implements IGetTrainingSessionUc {

    private final ITrainingSessionRepository trainingSessionRepository;

    @Override
    public TrainingSessionResponse getTrainingSessionById(String id, String currentUserId, boolean isManagement) {
        if (id == null || id.isBlank()) {
            throw new BusinessException(TrainingSessionError.TRAINING_SESSION_INVALID_DATA);
        }

        TrainingSessionEntity session = trainingSessionRepository.findById(id.trim())
                .orElseThrow(() -> new BusinessException(TrainingSessionError.TRAINING_SESSION_NOT_FOUND));

        if (!isManagement && !currentUserId.equals(session.getTrainerId())) {
            throw new BusinessException(TrainingSessionError.TRAINING_SESSION_ACCESS_DENIED);
        }

        return TrainingSessionResponse.fromEntity(session);
    }
}

