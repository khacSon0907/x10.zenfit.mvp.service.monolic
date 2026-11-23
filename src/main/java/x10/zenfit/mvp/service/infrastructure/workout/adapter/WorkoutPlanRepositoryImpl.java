package x10.zenfit.mvp.service.infrastructure.workout.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IWorkoutPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutPlanEntity;
import x10.zenfit.mvp.service.infrastructure.workout.adapter.mapper.WorkoutPlanMapper;
import x10.zenfit.mvp.service.infrastructure.workout.db.mongodb.WorkoutPlanDocument;
import x10.zenfit.mvp.service.infrastructure.workout.db.mongodb.WorkoutPlanMongoRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class WorkoutPlanRepositoryImpl implements IWorkoutPlanRepository {

    private final WorkoutPlanMongoRepository mongoRepository;

    @Override
    public WorkoutPlanEntity save(WorkoutPlanEntity entity) {
        if (entity == null) {
            return null;
        }
        WorkoutPlanDocument document = WorkoutPlanMapper.toDocument(entity);
        WorkoutPlanDocument saved = mongoRepository.save(document);
        return WorkoutPlanMapper.toEntity(saved);
    }

    @Override
    public Optional<WorkoutPlanEntity> findById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return mongoRepository.findById(id.trim()).map(WorkoutPlanMapper::toEntity);
    }

    @Override
    public Optional<WorkoutPlanEntity> findByIdAndTrainerId(String id, String trainerId) {
        if (id == null || id.isBlank() || trainerId == null || trainerId.isBlank()) {
            return Optional.empty();
        }
        return mongoRepository.findByIdAndTrainerId(id.trim(), trainerId.trim())
                .map(WorkoutPlanMapper::toEntity);
    }

    @Override
    public List<WorkoutPlanEntity> findByTrainerId(String trainerId) {
        if (trainerId == null || trainerId.isBlank()) {
            return List.of();
        }
        return mongoRepository.findByTrainerId(trainerId.trim())
                .stream()
                .map(WorkoutPlanMapper::toEntity)
                .toList();
    }

    @Override
    public List<WorkoutPlanEntity> findByClientId(String clientId) {
        if (clientId == null || clientId.isBlank()) {
            return List.of();
        }
        return mongoRepository.findByClientId(clientId.trim())
                .stream()
                .map(WorkoutPlanMapper::toEntity)
                .toList();
    }

    @Override
    public List<WorkoutPlanEntity> findByTrainerIdAndClientId(String trainerId, String clientId) {
        if (trainerId == null || trainerId.isBlank() || clientId == null || clientId.isBlank()) {
            return List.of();
        }
        return mongoRepository.findByTrainerIdAndClientId(trainerId.trim(), clientId.trim())
                .stream()
                .map(WorkoutPlanMapper::toEntity)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        if (id != null && !id.isBlank()) {
            mongoRepository.deleteById(id.trim());
        }
    }

    @Override
    public boolean existsById(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }
        return mongoRepository.existsById(id.trim());
    }
}

