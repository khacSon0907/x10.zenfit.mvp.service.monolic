package x10.zenfit.mvp.service.infrastructure.nutrition.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.INutritionPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionPlanEntity;
import x10.zenfit.mvp.service.infrastructure.nutrition.adapter.mapper.NutritionPlanMapper;
import x10.zenfit.mvp.service.infrastructure.nutrition.db.mongodb.NutritionPlanDocument;
import x10.zenfit.mvp.service.infrastructure.nutrition.db.mongodb.NutritionPlanMongoRepository;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class NutritionPlanRepositoryImpl implements INutritionPlanRepository {

    private final NutritionPlanMongoRepository mongoRepository;

    @Override
    public NutritionPlanEntity save(NutritionPlanEntity entity) {
        if (entity == null) {
            return null;
        }
        NutritionPlanDocument document = NutritionPlanMapper.toDocument(entity);
        NutritionPlanDocument saved = mongoRepository.save(document);
        return NutritionPlanMapper.toEntity(saved);
    }

    @Override
    public Optional<NutritionPlanEntity> findById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return mongoRepository.findById(id.trim()).map(NutritionPlanMapper::toEntity);
    }

    @Override
    public Optional<NutritionPlanEntity> findByIdAndTrainerId(String id, String trainerId) {
        if (id == null || id.isBlank() || trainerId == null || trainerId.isBlank()) {
            return Optional.empty();
        }
        return mongoRepository.findByIdAndTrainerId(id.trim(), trainerId.trim())
                .map(NutritionPlanMapper::toEntity);
    }

    @Override
    public List<NutritionPlanEntity> findByTrainerId(String trainerId) {
        if (trainerId == null || trainerId.isBlank()) {
            return List.of();
        }
        return mongoRepository.findByTrainerId(trainerId.trim())
                .stream()
                .map(NutritionPlanMapper::toEntity)
                .toList();
    }

    @Override
    public List<NutritionPlanEntity> findByClientId(String clientId) {
        if (clientId == null || clientId.isBlank()) {
            return List.of();
        }
        return mongoRepository.findByClientId(clientId.trim())
                .stream()
                .map(NutritionPlanMapper::toEntity)
                .toList();
    }

    @Override
    public List<NutritionPlanEntity> findByTrainerIdAndClientId(String trainerId, String clientId) {
        if (trainerId == null || trainerId.isBlank() || clientId == null || clientId.isBlank()) {
            return List.of();
        }
        return mongoRepository.findByTrainerIdAndClientId(trainerId.trim(), clientId.trim())
                .stream()
                .map(NutritionPlanMapper::toEntity)
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

