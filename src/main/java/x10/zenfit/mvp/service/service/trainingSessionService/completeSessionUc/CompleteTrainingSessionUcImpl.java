package x10.zenfit.mvp.service.service.trainingSessionService.completeSessionUc;

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

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class CompleteTrainingSessionUcImpl implements ICompleteTrainingSessionUc {

    private final ITrainingSessionRepository trainingSessionRepository;
    private final IPtContractRepository ptContractRepository;

    @Override
    public TrainingSessionResponse complete(String id, CompleteTrainingSessionRequest request, String currentUserId, boolean isManagement) {
        if (id == null || id.isBlank()) {
            throw new BusinessException(TrainingSessionError.TRAINING_SESSION_INVALID_DATA);
        }

        TrainingSessionEntity session = trainingSessionRepository.findById(id.trim())
                .orElseThrow(() -> new BusinessException(TrainingSessionError.TRAINING_SESSION_NOT_FOUND));

        // Kiểm tra quyền thao tác
        if (!isManagement && !currentUserId.equals(session.getTrainerId())) {
            throw new BusinessException(TrainingSessionError.TRAINING_SESSION_ACCESS_DENIED);
        }

        // Kiểm tra trạng thái: chỉ cho phép hoàn thành khi ở SCHEDULED
        if (session.getStatus() != TrainingSessionStatus.SCHEDULED) {
            throw new BusinessException(TrainingSessionError.TRAINING_SESSION_INVALID_STATUS);
        }

        LocalDateTime now = LocalDateTime.now();

        // Trừ 1 buổi tập trong hợp đồng PT liên kết với buổi tập này
        PtContractEntity contract = ptContractRepository.findById(session.getContractId())
                .orElseThrow(() -> new BusinessException(PtContractError.CONTRACT_NOT_FOUND));

        if (contract.getStatus() != ContractStatus.ACTIVE) {
            throw new BusinessException(TrainingSessionError.CONTRACT_NOT_ACTIVE);
        }

        if (contract.getRemainingSessions() <= 0) {
            throw new BusinessException(PtContractError.SESSIONS_EXCEEDED);
        }

        int currentUsed = contract.getUsedSessions() != null ? contract.getUsedSessions() : 0;
        contract.setUsedSessions(currentUsed + 1);
        contract.setUpdatedAt(now);

        // Nếu đã sử dụng hết toàn bộ số buổi (cả mua và tặng), chuyển trạng thái hợp đồng sang COMPLETED
        if (contract.getRemainingSessions() == 0) {
            contract.setStatus(ContractStatus.COMPLETED);
        }

        ptContractRepository.save(contract);

        // Cập nhật thông tin và trạng thái buổi tập sang COMPLETED
        session.setCompletedAt(now);
        session.setStatus(TrainingSessionStatus.COMPLETED);

        if (request != null) {
            if (request.getEvidenceImageUrls() != null) {
                session.setEvidenceImageUrls(request.getEvidenceImageUrls());
            }
            if (request.getNote() != null && !request.getNote().isBlank()) {
                session.setNote(request.getNote().trim());
            }
        }

        session.setUpdatedAt(now);

        TrainingSessionEntity saved = trainingSessionRepository.save(session);
        return TrainingSessionResponse.fromEntity(saved);
    }
}
