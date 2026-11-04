package x10.zenfit.mvp.service.service.clientGoalService.getGoalUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IClientGoalRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientGoalEntity;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.goal.ClientGoalError;

@Component
@RequiredArgsConstructor
public class GetClientGoalUcImpl implements IGetClientGoalUc {

    private final IClientGoalRepository clientGoalRepository;

    @Override
    public ClientGoalEntity getClientGoalById(String id) {
        if (id == null || id.isBlank()) {
            throw new BusinessException(ClientGoalError.INVALID_CLIENT_GOAL_DATA);
        }

        return clientGoalRepository.findById(id.trim())
                .orElseThrow(() -> new BusinessException(ClientGoalError.CLIENT_GOAL_NOT_FOUND));
    }
}

