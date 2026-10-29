package x10.zenfit.mvp.service.Repository;

import x10.zenfit.mvp.service.common.domain.entities.client.ClientGoalEntity;
import x10.zenfit.mvp.service.common.domain.enums.GoalPhaseStatus;

import java.util.List;
import java.util.Optional;

public interface IClientGoalRepository {

    ClientGoalEntity save(ClientGoalEntity goal);

    Optional<ClientGoalEntity> findById(String id);

    List<ClientGoalEntity> findByClientId(String clientId);

    List<ClientGoalEntity> findByClientIdAndStatus(String clientId, GoalPhaseStatus status);

    Optional<ClientGoalEntity> findFirstByClientIdOrderByCreatedAtDesc(String clientId);

    Optional<ClientGoalEntity> findActiveGoalByClientId(String clientId);

    void deleteById(String id);
}
