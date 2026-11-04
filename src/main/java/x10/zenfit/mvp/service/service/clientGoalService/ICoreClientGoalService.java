package x10.zenfit.mvp.service.service.clientGoalService;

import x10.zenfit.mvp.service.common.domain.entities.client.ClientGoalEntity;
import x10.zenfit.mvp.service.common.domain.enums.GoalPhaseStatus;
import x10.zenfit.mvp.service.service.clientGoalService.createGoalUc.CreateClientGoalRequest;
import x10.zenfit.mvp.service.service.clientGoalService.updateGoalUc.UpdateClientGoalRequest;

import java.util.List;

public interface ICoreClientGoalService {

    ClientGoalEntity createClientGoal(CreateClientGoalRequest request);

    ClientGoalEntity getClientGoalById(String id);

    List<ClientGoalEntity> getAllClientGoals(String clientId, GoalPhaseStatus status);

    List<ClientGoalEntity> getClientGoalsByClientId(String clientId);

    ClientGoalEntity getActiveGoalByClientId(String clientId);

    ClientGoalEntity updateClientGoal(String id, UpdateClientGoalRequest request);

    void deleteClientGoal(String id);
}
