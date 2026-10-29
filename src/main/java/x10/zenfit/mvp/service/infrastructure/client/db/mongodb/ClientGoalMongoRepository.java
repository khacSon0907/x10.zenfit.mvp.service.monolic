package x10.zenfit.mvp.service.infrastructure.client.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.common.domain.enums.GoalPhaseStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientGoalMongoRepository extends MongoRepository<ClientGoalDocument, String> {

    List<ClientGoalDocument> findByClientId(String clientId);

    List<ClientGoalDocument> findByClientIdAndStatus(String clientId, GoalPhaseStatus status);

    Optional<ClientGoalDocument> findFirstByClientIdOrderByCreatedAtDesc(String clientId);

    Optional<ClientGoalDocument> findFirstByClientIdAndStatusOrderByCreatedAtDesc(String clientId, GoalPhaseStatus status);
}
