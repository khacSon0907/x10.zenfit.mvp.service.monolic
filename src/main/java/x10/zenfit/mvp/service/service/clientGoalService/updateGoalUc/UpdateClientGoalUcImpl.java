package x10.zenfit.mvp.service.service.clientGoalService.updateGoalUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IClientGoalRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientGoalEntity;
import x10.zenfit.mvp.service.common.domain.enums.GoalPhaseStatus;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.goal.ClientGoalError;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class UpdateClientGoalUcImpl implements IUpdateClientGoalUc {

    private final IClientGoalRepository clientGoalRepository;

    @Override
    public ClientGoalEntity updateClientGoal(String id, UpdateClientGoalRequest request) {
        if (id == null || id.isBlank()) {
            throw new BusinessException(ClientGoalError.INVALID_CLIENT_GOAL_DATA);
        }
        if (request == null) {
            throw new BusinessException(ClientGoalError.INVALID_CLIENT_GOAL_DATA);
        }

        ClientGoalEntity existing = clientGoalRepository.findById(id.trim())
                .orElseThrow(() -> new BusinessException(ClientGoalError.CLIENT_GOAL_NOT_FOUND));

        LocalDate newStartDate = request.getStartDate() != null ? request.getStartDate() : existing.getStartDate();
        LocalDate newEndDate = request.getEndDate() != null ? request.getEndDate() : existing.getEndDate();

        if (newStartDate != null && newEndDate != null && newEndDate.isBefore(newStartDate)) {
            throw new BusinessException(ClientGoalError.CLIENT_GOAL_INVALID_DATE_RANGE);
        }

        if (request.getGoalName() != null && !request.getGoalName().isBlank()) {
            existing.setGoalName(request.getGoalName().trim());
        }
        if (request.getGoalType() != null && !request.getGoalType().isBlank()) {
            existing.setGoalType(request.getGoalType().trim());
        }
        if (request.getTrainerId() != null && !request.getTrainerId().isBlank()) {
            existing.setTrainerId(request.getTrainerId().trim());
        }
        if (request.getStartDate() != null) {
            existing.setStartDate(request.getStartDate());
        }
        if (request.getEndDate() != null) {
            existing.setEndDate(request.getEndDate());
        }
        if (request.getActualEndDate() != null) {
            existing.setActualEndDate(request.getActualEndDate());
        }
        if (request.getStartWeight() != null) {
            existing.setStartWeight(request.getStartWeight());
        }
        if (request.getTargetWeight() != null) {
            existing.setTargetWeight(request.getTargetWeight());
        }
        if (request.getEndWeight() != null) {
            existing.setEndWeight(request.getEndWeight());
        }
        if (request.getNote() != null) {
            existing.setNote(request.getNote());
        }

        if (request.getStatus() != null) {
            // Tự động gán ngày kết thúc thực tế nếu hoàn thành mục tiêu mà chưa có actualEndDate
            if (request.getStatus() == GoalPhaseStatus.COMPLETED && existing.getActualEndDate() == null) {
                existing.setActualEndDate(request.getActualEndDate() != null ? request.getActualEndDate() : LocalDate.now());
            }
            existing.setStatus(request.getStatus());
        }

        existing.setUpdatedAt(LocalDateTime.now());

        return clientGoalRepository.save(existing);
    }
}

