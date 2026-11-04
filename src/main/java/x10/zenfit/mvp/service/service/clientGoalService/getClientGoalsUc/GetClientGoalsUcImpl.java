package x10.zenfit.mvp.service.service.clientGoalService.getClientGoalsUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IClientGoalRepository;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientGoalEntity;
import x10.zenfit.mvp.service.common.domain.enums.GoalPhaseStatus;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.client.ClientError;
import x10.zenfit.mvp.service.share.exception.goal.ClientGoalError;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetClientGoalsUcImpl implements IGetClientGoalsUc {

    private final IClientGoalRepository clientGoalRepository;
    private final IClientRepository clientRepository;

    @Override
    public List<ClientGoalEntity> getAllClientGoals(String clientId, GoalPhaseStatus status) {
        if (clientId != null && !clientId.isBlank()) {
            String trimmedClientId = clientId.trim();
            if (status != null) {
                return clientGoalRepository.findByClientIdAndStatus(trimmedClientId, status);
            }
            return clientGoalRepository.findByClientId(trimmedClientId);
        }

        List<ClientGoalEntity> allGoals = clientGoalRepository.findAll();
        if (status != null) {
            return allGoals.stream()
                    .filter(g -> g.getStatus() == status)
                    .toList();
        }
        return allGoals;
    }

    @Override
    public List<ClientGoalEntity> getClientGoalsByClientId(String clientId) {
        if (clientId == null || clientId.isBlank()) {
            throw new BusinessException(ClientGoalError.INVALID_CLIENT_GOAL_DATA);
        }

        String trimmedClientId = clientId.trim();
        clientRepository.findById(trimmedClientId)
                .orElseThrow(() -> new BusinessException(ClientError.CLIENT_NOT_FOUND));

        return clientGoalRepository.findByClientId(trimmedClientId);
    }

    @Override
    public ClientGoalEntity getActiveGoalByClientId(String clientId) {
        if (clientId == null || clientId.isBlank()) {
            throw new BusinessException(ClientGoalError.INVALID_CLIENT_GOAL_DATA);
        }

        String trimmedClientId = clientId.trim();
        clientRepository.findById(trimmedClientId)
                .orElseThrow(() -> new BusinessException(ClientError.CLIENT_NOT_FOUND));

        return clientGoalRepository.findActiveGoalByClientId(trimmedClientId)
                .orElseThrow(() -> new BusinessException(ClientGoalError.CLIENT_GOAL_NOT_FOUND));
    }
}

