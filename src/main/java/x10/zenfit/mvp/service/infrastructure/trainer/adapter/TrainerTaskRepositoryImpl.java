package x10.zenfit.mvp.service.infrastructure.trainer.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.ITrainerTaskRepository;
import x10.zenfit.mvp.service.common.domain.entities.trainer.TrainerTaskEntity;
import x10.zenfit.mvp.service.common.domain.enums.TrainerTaskStatus;
import x10.zenfit.mvp.service.infrastructure.trainer.adapter.mapper.TrainerTaskMapper;
import x10.zenfit.mvp.service.infrastructure.trainer.db.mongodb.TrainerTaskDocument;
import x10.zenfit.mvp.service.infrastructure.trainer.db.mongodb.TrainerTaskMongoRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TrainerTaskRepositoryImpl implements ITrainerTaskRepository {

    private final TrainerTaskMongoRepository mongoRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public TrainerTaskEntity save(TrainerTaskEntity task) {
        if (task == null) {
            return null;
        }
        TrainerTaskDocument document = TrainerTaskMapper.toDocument(task);
        TrainerTaskDocument saved = mongoRepository.save(document);
        return TrainerTaskMapper.toEntity(saved);
    }

    @Override
    public Optional<TrainerTaskEntity> findById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return mongoRepository.findById(id.trim()).map(TrainerTaskMapper::toEntity);
    }

    @Override
    public boolean existsById(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }
        return mongoRepository.existsById(id.trim());
    }

    @Override
    public void deleteById(String id) {
        if (id != null && !id.isBlank()) {
            mongoRepository.deleteById(id.trim());
        }
    }

    @Override
    public List<TrainerTaskEntity> findTasks(
            String trainerId,
            LocalDate taskDate,
            LocalDate startDate,
            LocalDate endDate,
            TrainerTaskStatus status
    ) {
        List<Criteria> criteriaList = new ArrayList<>();

        if (trainerId != null && !trainerId.isBlank()) {
            criteriaList.add(Criteria.where("trainerId").is(trainerId.trim()));
        }

        if (taskDate != null) {
            criteriaList.add(Criteria.where("taskDate").is(taskDate));
        } else if (startDate != null && endDate != null) {
            criteriaList.add(Criteria.where("taskDate").gte(startDate).lte(endDate));
        } else if (startDate != null) {
            criteriaList.add(Criteria.where("taskDate").gte(startDate));
        } else if (endDate != null) {
            criteriaList.add(Criteria.where("taskDate").lte(endDate));
        }

        if (status != null) {
            criteriaList.add(Criteria.where("status").is(status));
        }

        Query query = new Query();
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        query.with(Sort.by(Sort.Direction.ASC, "taskDate").and(Sort.by(Sort.Direction.ASC, "startTime")));

        return mongoTemplate.find(query, TrainerTaskDocument.class)
                .stream()
                .map(TrainerTaskMapper::toEntity)
                .toList();
    }
}

