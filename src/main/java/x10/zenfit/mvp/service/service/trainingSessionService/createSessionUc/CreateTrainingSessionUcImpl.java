package x10.zenfit.mvp.service.service.trainingSessionService.createSessionUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.Repository.IPtContractRepository;
import x10.zenfit.mvp.service.Repository.ITrainingSessionRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.common.domain.entities.contract.PtContractEntity;
import x10.zenfit.mvp.service.common.domain.entities.session.TrainingSessionEntity;
import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;
import x10.zenfit.mvp.service.common.domain.enums.TrainingSessionStatus;
import x10.zenfit.mvp.service.service.trainingSessionService.TrainingSessionResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.client.ClientError;
import x10.zenfit.mvp.service.share.exception.contract.PtContractError;
import x10.zenfit.mvp.service.share.exception.session.TrainingSessionError;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CreateTrainingSessionUcImpl implements ICreateTrainingSessionUc {

    private final ITrainingSessionRepository trainingSessionRepository;
    private final IClientRepository clientRepository;
    private final IPtContractRepository ptContractRepository;

    @Override
    public TrainingSessionResponse createTrainingSession(CreateTrainingSessionRequest request, String currentUserId, boolean isManagement) {
        if (request == null) {
            throw new BusinessException(TrainingSessionError.TRAINING_SESSION_INVALID_DATA);
        }

        // 1. Kiểm tra học viên tồn tại
        ClientEntity client = clientRepository.findById(request.getClientId().trim())
                .orElseThrow(() -> new BusinessException(ClientError.CLIENT_NOT_FOUND));

        // 2. Kiểm tra hợp đồng tồn tại
        PtContractEntity contract = ptContractRepository.findById(request.getContractId().trim())
                .orElseThrow(() -> new BusinessException(PtContractError.CONTRACT_NOT_FOUND));

        // 3. Kiểm tra hợp đồng có thuộc về học viên không
        if (!client.getId().equals(contract.getClientId())) {
            throw new BusinessException(TrainingSessionError.CONTRACT_NOT_BELONG_TO_CLIENT);
        }

        // 4. Kiểm tra trạng thái hợp đồng (phải ACTIVE và còn buổi tập)
        if (contract.getStatus() != ContractStatus.ACTIVE) {
            throw new BusinessException(TrainingSessionError.CONTRACT_NOT_ACTIVE);
        }
        if (contract.getRemainingSessions() <= 0) {
            throw new BusinessException(PtContractError.SESSIONS_EXCEEDED);
        }

        // 5. Xác định trainerId
        String trainerId;
        if (!isManagement) {
            // PT chỉ được tạo buổi tập cho chính mình
            trainerId = currentUserId;
        } else {
            // Quản lý/Admin có thể chỉ định PT hoặc mặc định theo hợp đồng / học viên
            if (request.getTrainerId() != null && !request.getTrainerId().isBlank()) {
                trainerId = request.getTrainerId().trim();
            } else if (contract.getTrainerId() != null && !contract.getTrainerId().isBlank()) {
                trainerId = contract.getTrainerId();
            } else if (client.getAssignedTrainerId() != null && !client.getAssignedTrainerId().isBlank()) {
                trainerId = client.getAssignedTrainerId();
            } else {
                trainerId = currentUserId;
            }
        }

        // 6. Tính toán và kiểm tra thời gian bắt đầu và kết thúc
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDateTime = LocalDateTime.of(request.getTrainingDate(), request.getStartTime());

        // Thời gian bắt đầu buổi tập bắt buộc phải lớn hơn thời gian hiện tại
        if (!startDateTime.isAfter(now)) {
            throw new BusinessException(TrainingSessionError.TRAINING_SESSION_IN_PAST);
        }

        LocalDateTime endTime = request.getEndTime();
        if (endTime != null) {
            if (!endTime.isAfter(startDateTime)) {
                throw new BusinessException(TrainingSessionError.TRAINING_SESSION_INVALID_DATA);
            }
        } else {
            endTime = startDateTime.plusHours(1);
        }

        // 7. Kiểm tra trùng lịch PT
        if (trainingSessionRepository.existsTrainerConflict(trainerId, request.getTrainingDate(), request.getStartTime(), endTime, null)) {
            throw new BusinessException(TrainingSessionError.TRAINING_SESSION_CONFLICT);
        }

        // 8. Kiểm tra trùng lịch học viên
        if (trainingSessionRepository.existsClientConflict(client.getId(), request.getTrainingDate(), request.getStartTime(), endTime, null)) {
            throw new BusinessException(TrainingSessionError.TRAINING_SESSION_CONFLICT);
        }

        // 9. Khởi tạo và lưu buổi tập
        TrainingSessionEntity entity = TrainingSessionEntity.builder()
                .clientId(client.getId())
                .trainerId(trainerId)
                .contractId(contract.getId())
                .sessionName(request.getSessionName().trim())
                .trainingDate(request.getTrainingDate())
                .startTime(request.getStartTime())
                .endTime(endTime)
                .status(TrainingSessionStatus.SCHEDULED)
                .checkInAt(null)
                .completedAt(null)
                .evidenceImageUrls(request.getEvidenceImageUrls())
                .note(request.getNote() != null ? request.getNote().trim() : null)
                .createdAt(now)
                .updatedAt(now)
                .build();

        TrainingSessionEntity saved = trainingSessionRepository.save(entity);
        return TrainingSessionResponse.fromEntity(saved);
    }
}

