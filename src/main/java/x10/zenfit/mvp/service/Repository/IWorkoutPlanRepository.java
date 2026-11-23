package x10.zenfit.mvp.service.Repository;

import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutPlanEntity;

import java.util.List;
import java.util.Optional;

public interface IWorkoutPlanRepository {

    WorkoutPlanEntity save(WorkoutPlanEntity entity);

    Optional<WorkoutPlanEntity> findById(String id);

    Optional<WorkoutPlanEntity> findByIdAndTrainerId(String id, String trainerId);

    List<WorkoutPlanEntity> findByTrainerId(String trainerId);

    List<WorkoutPlanEntity> findByClientId(String clientId);

    List<WorkoutPlanEntity> findByTrainerIdAndClientId(String trainerId, String clientId);

    void deleteById(String id);

    boolean existsById(String id);
}

