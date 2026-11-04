package x10.zenfit.mvp.service.service.clientGoalService.createGoalUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IClientGoalRepository;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientGoalEntity;
import x10.zenfit.mvp.service.common.domain.enums.GoalPhaseStatus;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.client.ClientError;
import x10.zenfit.mvp.service.share.exception.goal.ClientGoalError;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CreateClientGoalImpl implements ICreateClientGoalUc {

    private final IClientGoalRepository clientGoalRepository;
    private final IClientRepository clientRepository;

    @Override
    public ClientGoalEntity execute(CreateClientGoalRequest request) {
        if (request == null) {
            throw new BusinessException(ClientGoalError.INVALID_CLIENT_GOAL_DATA);
        }

        // Kiểm tra khách hàng tồn tại
        clientRepository.findById(request.getClientId().trim())
                .orElseThrow(() -> new BusinessException(ClientError.CLIENT_NOT_FOUND));

        // Kiểm tra ngày bắt đầu và kết thúc
        if (request.getEndDate().isBefore(request.getStartDate())) {
            throw new BusinessException(ClientGoalError.CLIENT_GOAL_INVALID_DATE_RANGE);
        }

        GoalPhaseStatus status = request.getStatus();
        if (status == null) {
            status = request.getStartDate().isAfter(LocalDate.now())
                    ? GoalPhaseStatus.PLANNED
                    : GoalPhaseStatus.ACTIVE;
        }

        ClientGoalEntity goal = ClientGoalEntity.builder()
                .id(UUID.randomUUID().toString())
                .clientId(request.getClientId().trim())
                .trainerId(request.getTrainerId().trim())
                .goalName(request.getGoalName().trim())
                .goalType(request.getGoalType())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .startWeight(request.getStartWeight())
                .targetWeight(request.getTargetWeight())
                .status(status)
                .note(request.getNote())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        return clientGoalRepository.save(goal);
    }
}
