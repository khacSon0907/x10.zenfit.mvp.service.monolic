package x10.zenfit.mvp.service.service.clientGoalService.updateGoalUc;

import x10.zenfit.mvp.service.common.domain.entities.client.ClientGoalEntity;

public interface IUpdateClientGoalUc {

    ClientGoalEntity updateClientGoal(String id, UpdateClientGoalRequest request);
}

