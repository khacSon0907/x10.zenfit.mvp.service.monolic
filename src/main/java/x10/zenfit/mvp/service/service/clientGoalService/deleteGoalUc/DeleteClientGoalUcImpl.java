package x10.zenfit.mvp.service.service.clientGoalService.deleteGoalUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IClientGoalRepository;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.goal.ClientGoalError;

@Component
@RequiredArgsConstructor
public class DeleteClientGoalUcImpl implements IDeleteClientGoalUc {

    private final IClientGoalRepository clientGoalRepository;

    @Override
    public void deleteClientGoal(String id) {
        if (id == null || id.isBlank()) {
            throw new BusinessException(ClientGoalError.INVALID_CLIENT_GOAL_DATA);
        }

        String trimmedId = id.trim();
        if (!clientGoalRepository.existsById(trimmedId)) {
            throw new BusinessException(ClientGoalError.CLIENT_GOAL_NOT_FOUND);
        }

        clientGoalRepository.deleteById(trimmedId);
    }
}

