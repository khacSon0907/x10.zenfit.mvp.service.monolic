package x10.zenfit.mvp.service.service.trainingSessionService.updateSessionUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IPtContractRepository;
import x10.zenfit.mvp.service.Repository.ITrainingSessionRepository;
import x10.zenfit.mvp.service.common.domain.entities.contract.PtContractEntity;
import x10.zenfit.mvp.service.common.domain.entities.session.TrainingSessionEntity;
import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;
import x10.zenfit.mvp.service.common.domain.enums.TrainingSessionStatus;
import x10.zenfit.mvp.service.service.trainingSessionService.TrainingSessionResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.contract.PtContractError;
import x10.zenfit.mvp.service.share.exception.session.TrainingSessionError;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class UpdateTrainingSessionUcImpl implements IUpdateTrainingSessionUc {

    private final ITrainingSessionRepository trainingSessionRepository;
    private final IPtContractRepository ptContractRepository;

    @Override
    public TrainingSessionResponse updateTrainingSession(String id, UpdateTrainingSessionRequest request, String currentUserId, boolean isManagement) {
        if (id == null || id.isBlank() || request == null) {
            throw new BusinessException(TrainingSessionError.TRAINING_SESSION_INVALID_DATA);
        }

        TrainingSessionEntity session = trainingSessionRepository.findById(id.trim())
                .orElseThrow(() -> new BusinessException(TrainingSessionError.TRAINING_SESSION_NOT_FOUND));

        // 1. Kiểm tra quyền thao tác
        if (!isManagement && !currentUserId.equals(session.getTrainerId())) {
            throw new BusinessException(TrainingSessionError.TRAINING_SESSION_ACCESS_DENIED);
        }

        // 2. Kiểm tra trạng thái: cấm cập nhật nếu buổi tập đã hoàn thành hoặc đã hủy
        if (session.getStatus() == TrainingSessionStatus.COMPLETED
                || session.getStatus() == TrainingSessionStatus.CANCELLED) {
            throw new BusinessException(TrainingSessionError.TRAINING_SESSION_INVALID_STATUS);
        }

        LocalDate newDate = request.getTrainingDate() != null ? request.getTrainingDate() : session.getTrainingDate();
        LocalTime newTime = request.getStartTime() != null ? request.getStartTime() : session.getStartTime();
        String newTrainerId = (request.getTrainerId() != null && !request.getTrainerId().isBlank() && isManagement)
                ? request.getTrainerId().trim()
                : session.getTrainerId();
        String newClientId = (request.getClientId() != null && !request.getClientId().isBlank() && isManagement)
                ? request.getClientId().trim()
                : session.getClientId();

        // Tính toán và kiểm tra thời gian bắt đầu và kết thúc
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime newStartDateTime = LocalDateTime.of(newDate, newTime);

        boolean scheduleTimeChanged = !newDate.equals(session.getTrainingDate()) || !newTime.equals(session.getStartTime());
        if (scheduleTimeChanged && !newStartDateTime.isAfter(now)) {
            throw new BusinessException(TrainingSessionError.TRAINING_SESSION_IN_PAST);
        }
        LocalDateTime newEndDateTime;
        if (request.getEndTime() != null) {
            newEndDateTime = request.getEndTime();
        } else if (session.getEndTime() != null) {
            if (!newDate.equals(session.getTrainingDate()) || !newTime.equals(session.getStartTime())) {
                LocalDateTime oldStartDateTime = LocalDateTime.of(session.getTrainingDate(), session.getStartTime());
                long durationMinutes = java.time.Duration.between(oldStartDateTime, session.getEndTime()).toMinutes();
                if (durationMinutes <= 0) {
                    durationMinutes = 60;
                }
                newEndDateTime = newStartDateTime.plusMinutes(durationMinutes);
            } else {
                newEndDateTime = session.getEndTime();
            }
        } else {
            newEndDateTime = newStartDateTime.plusHours(1);
        }

        if (!newEndDateTime.isAfter(newStartDateTime)) {
            throw new BusinessException(TrainingSessionError.TRAINING_SESSION_INVALID_DATA);
        }

        // 3. Nếu thời gian, PT hoặc học viên thay đổi -> Kiểm tra lại xung đột
        boolean timeOrPartyChanged = !newDate.equals(session.getTrainingDate())
                || !newTime.equals(session.getStartTime())
                || !java.util.Objects.equals(newEndDateTime, session.getEndTime())
                || !newTrainerId.equals(session.getTrainerId())
                || !newClientId.equals(session.getClientId());

        if (timeOrPartyChanged) {
            if (trainingSessionRepository.existsTrainerConflict(newTrainerId, newDate, newTime, newEndDateTime, session.getId())) {
                throw new BusinessException(TrainingSessionError.TRAINING_SESSION_CONFLICT);
            }
            if (trainingSessionRepository.existsClientConflict(newClientId, newDate, newTime, newEndDateTime, session.getId())) {
                throw new BusinessException(TrainingSessionError.TRAINING_SESSION_CONFLICT);
            }
        }

        // 4. Nếu đổi hợp đồng
        if (request.getContractId() != null && !request.getContractId().isBlank() && !request.getContractId().equals(session.getContractId())) {
            PtContractEntity contract = ptContractRepository.findById(request.getContractId().trim())
                    .orElseThrow(() -> new BusinessException(PtContractError.CONTRACT_NOT_FOUND));

            if (!newClientId.equals(contract.getClientId())) {
                throw new BusinessException(TrainingSessionError.CONTRACT_NOT_BELONG_TO_CLIENT);
            }
            if (contract.getStatus() != ContractStatus.ACTIVE) {
                throw new BusinessException(TrainingSessionError.CONTRACT_NOT_ACTIVE);
            }
            if (contract.getRemainingSessions() <= 0) {
                throw new BusinessException(PtContractError.SESSIONS_EXCEEDED);
            }
            session.setContractId(contract.getId());
        }

        // 5. Cập nhật các trường
        session.setTrainingDate(newDate);
        session.setStartTime(newTime);
        session.setEndTime(newEndDateTime);
        session.setTrainerId(newTrainerId);
        session.setClientId(newClientId);

        if (request.getSessionName() != null && !request.getSessionName().isBlank()) {
            session.setSessionName(request.getSessionName().trim());
        }
        if (request.getNote() != null) {
            session.setNote(request.getNote().trim());
        }
        if (request.getEvidenceImageUrls() != null) {
            session.setEvidenceImageUrls(request.getEvidenceImageUrls());
        }

        session.setUpdatedAt(LocalDateTime.now());

        TrainingSessionEntity saved = trainingSessionRepository.save(session);
        return TrainingSessionResponse.fromEntity(saved);
    }
}

