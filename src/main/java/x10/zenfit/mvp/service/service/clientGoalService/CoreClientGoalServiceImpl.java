package x10.zenfit.mvp.service.service.clientGoalService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientGoalEntity;
import x10.zenfit.mvp.service.common.domain.enums.GoalPhaseStatus;
import x10.zenfit.mvp.service.service.clientGoalService.createGoalUc.CreateClientGoalRequest;
import x10.zenfit.mvp.service.service.clientGoalService.createGoalUc.ICreateClientGoalUc;
import x10.zenfit.mvp.service.service.clientGoalService.deleteGoalUc.IDeleteClientGoalUc;
import x10.zenfit.mvp.service.service.clientGoalService.getClientGoalsUc.IGetClientGoalsUc;
import x10.zenfit.mvp.service.service.clientGoalService.getGoalUc.IGetClientGoalUc;
import x10.zenfit.mvp.service.service.clientGoalService.updateGoalUc.IUpdateClientGoalUc;
import x10.zenfit.mvp.service.service.clientGoalService.updateGoalUc.UpdateClientGoalRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoreClientGoalServiceImpl implements ICoreClientGoalService {

    private final ICreateClientGoalUc createClientGoalUc;
    private final IGetClientGoalUc getClientGoalUc;
    private final IGetClientGoalsUc getClientGoalsUc;
    private final IUpdateClientGoalUc updateClientGoalUc;
    private final IDeleteClientGoalUc deleteClientGoalUc;

    @Override
    public ClientGoalEntity createClientGoal(CreateClientGoalRequest request) {
        return createClientGoalUc.execute(request);
    }

    @Override
    public ClientGoalEntity getClientGoalById(String id) {
        return getClientGoalUc.getClientGoalById(id);
    }

    @Override
    public List<ClientGoalEntity> getAllClientGoals(String clientId, GoalPhaseStatus status) {
        return getClientGoalsUc.getAllClientGoals(clientId, status);
    }

    @Override
    public List<ClientGoalEntity> getClientGoalsByClientId(String clientId) {
        return getClientGoalsUc.getClientGoalsByClientId(clientId);
    }

    @Override
    public ClientGoalEntity getActiveGoalByClientId(String clientId) {
        return getClientGoalsUc.getActiveGoalByClientId(clientId);
    }

    @Override
    public ClientGoalEntity updateClientGoal(String id, UpdateClientGoalRequest request) {
        return updateClientGoalUc.updateClientGoal(id, request);
    }

    @Override
    public void deleteClientGoal(String id) {
        deleteClientGoalUc.deleteClientGoal(id);
    }
}
