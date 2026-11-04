package x10.zenfit.mvp.service.service.clientGoalService.createGoalUc;

import x10.zenfit.mvp.service.common.domain.entities.client.ClientGoalEntity;

public interface ICreateClientGoalUc {

    ClientGoalEntity execute(CreateClientGoalRequest request);

}