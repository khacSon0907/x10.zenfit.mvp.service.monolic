package x10.zenfit.mvp.service.service.trainingSessionService.cancelSessionUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.ITrainingSessionRepository;
import x10.zenfit.mvp.service.common.domain.entities.session.TrainingSessionEntity;
import x10.zenfit.mvp.service.common.domain.enums.TrainingSessionStatus;
import x10.zenfit.mvp.service.service.trainingSessionService.TrainingSessionResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.session.TrainingSessionError;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CancelTrainingSessionUcImpl implements ICancelTrainingSessionUc {

    private final ITrainingSessionRepository trainingSessionRepository;

    @Override
    public TrainingSessionResponse cancel(String id, String currentUserId, boolean isManagement) {
        if (id == null || id.isBlank()) {
            throw new BusinessException(TrainingSessionError.TRAINING_SESSION_INVALID_DATA);
        }

        TrainingSessionEntity session = trainingSessionRepository.findById(id.trim())
                .orElseThrow(() -> new BusinessException(TrainingSessionError.TRAINING_SESSION_NOT_FOUND));

        // Kiểm tra quyền thao tác
        if (!isManagement && !currentUserId.equals(session.getTrainerId())) {
            throw new BusinessException(TrainingSessionError.TRAINING_SESSION_ACCESS_DENIED);
        }

        // Kiểm tra trạng thái: không thể hủy buổi tập đã COMPLETED hoặc CANCELLED
        if (session.getStatus() == TrainingSessionStatus.COMPLETED
                || session.getStatus() == TrainingSessionStatus.CANCELLED) {
            throw new BusinessException(TrainingSessionError.TRAINING_SESSION_INVALID_STATUS);
        }

        session.setStatus(TrainingSessionStatus.CANCELLED);
        session.setUpdatedAt(LocalDateTime.now());

        TrainingSessionEntity saved = trainingSessionRepository.save(session);
        return TrainingSessionResponse.fromEntity(saved);
    }
}

