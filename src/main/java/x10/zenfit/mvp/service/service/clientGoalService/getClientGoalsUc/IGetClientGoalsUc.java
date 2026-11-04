package x10.zenfit.mvp.service.service.clientGoalService.getClientGoalsUc;

import x10.zenfit.mvp.service.common.domain.entities.client.ClientGoalEntity;
import x10.zenfit.mvp.service.common.domain.enums.GoalPhaseStatus;

import java.util.List;

public interface IGetClientGoalsUc {

    List<ClientGoalEntity> getAllClientGoals(String clientId, GoalPhaseStatus status);

    List<ClientGoalEntity> getClientGoalsByClientId(String clientId);

    ClientGoalEntity getActiveGoalByClientId(String clientId);
}

