package x10.zenfit.mvp.service.Repository;

import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionPlanEntity;

import java.util.List;
import java.util.Optional;

public interface INutritionPlanRepository {

    NutritionPlanEntity save(NutritionPlanEntity entity);

    Optional<NutritionPlanEntity> findById(String id);

    Optional<NutritionPlanEntity> findByIdAndTrainerId(String id, String trainerId);

    List<NutritionPlanEntity> findByTrainerId(String trainerId);

    List<NutritionPlanEntity> findByClientId(String clientId);

    List<NutritionPlanEntity> findByTrainerIdAndClientId(String trainerId, String clientId);

    void deleteById(String id);

    boolean existsById(String id);
}

