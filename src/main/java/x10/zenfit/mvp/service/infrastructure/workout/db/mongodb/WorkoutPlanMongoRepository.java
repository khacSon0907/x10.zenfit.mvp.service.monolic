package x10.zenfit.mvp.service.infrastructure.workout.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutPlanMongoRepository extends MongoRepository<WorkoutPlanDocument, String> {

    List<WorkoutPlanDocument> findByTrainerId(String trainerId);

    List<WorkoutPlanDocument> findByClientId(String clientId);

    List<WorkoutPlanDocument> findByTrainerIdAndClientId(String trainerId, String clientId);

    Optional<WorkoutPlanDocument> findByIdAndTrainerId(String id, String trainerId);
}

