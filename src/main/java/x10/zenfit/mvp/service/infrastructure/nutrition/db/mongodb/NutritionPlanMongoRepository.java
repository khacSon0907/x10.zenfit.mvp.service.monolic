package x10.zenfit.mvp.service.infrastructure.nutrition.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NutritionPlanMongoRepository extends MongoRepository<NutritionPlanDocument, String> {

    List<NutritionPlanDocument> findByTrainerId(String trainerId);

    List<NutritionPlanDocument> findByClientId(String clientId);

    List<NutritionPlanDocument> findByTrainerIdAndClientId(String trainerId, String clientId);

    Optional<NutritionPlanDocument> findByIdAndTrainerId(String id, String trainerId);
}

