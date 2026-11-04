package x10.zenfit.mvp.service.infrastructure.session.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.Repository.ITrainingSessionRepository;
import x10.zenfit.mvp.service.common.domain.entities.session.TrainingSessionEntity;
import x10.zenfit.mvp.service.common.domain.enums.TrainingSessionStatus;
import x10.zenfit.mvp.service.infrastructure.session.adapter.mapper.TrainingSessionMapper;
import x10.zenfit.mvp.service.infrastructure.session.db.mongodb.TrainingSessionDocument;
import x10.zenfit.mvp.service.infrastructure.session.db.mongodb.TrainingSessionMongoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TrainingSessionRepositoryImpl implements ITrainingSessionRepository {

    private final TrainingSessionMongoRepository mongoRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public TrainingSessionEntity save(TrainingSessionEntity session) {
        TrainingSessionDocument document = TrainingSessionMapper.toDocument(session);
        TrainingSessionDocument saved = mongoRepository.save(document);
        return TrainingSessionMapper.toEntity(saved);
    }

    @Override
    public Optional<TrainingSessionEntity> findById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        return mongoRepository.findById(id.trim())
                .map(TrainingSessionMapper::toEntity);
    }

    @Override
    public boolean existsById(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }
        return mongoRepository.existsById(id.trim());
    }

    @Override
    public boolean existsTrainerConflict(String trainerId, LocalDate trainingDate, LocalTime startTime, LocalDateTime endTime, String excludeSessionId) {
        if (trainerId == null || trainingDate == null || startTime == null) {
            return false;
        }

        LocalDateTime newStart = LocalDateTime.of(trainingDate, startTime);
        LocalDateTime newEnd = (endTime != null && endTime.isAfter(newStart)) ? endTime : newStart.plusHours(1);

        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(Criteria.where("trainerId").is(trainerId.trim()));
        criteriaList.add(Criteria.where("trainingDate").is(trainingDate));
        criteriaList.add(Criteria.where("status").ne(TrainingSessionStatus.CANCELLED));

        if (excludeSessionId != null && !excludeSessionId.isBlank()) {
            criteriaList.add(Criteria.where("id").ne(excludeSessionId.trim()));
        }

        Query query = new Query(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        List<TrainingSessionDocument> existingSessions = mongoTemplate.find(query, TrainingSessionDocument.class);

        return existingSessions.stream().anyMatch(s -> {
            LocalDateTime sStart = LocalDateTime.of(s.getTrainingDate(), s.getStartTime());
            LocalDateTime sEnd = (s.getEndTime() != null && s.getEndTime().isAfter(sStart)) ? s.getEndTime() : sStart.plusHours(1);
            return newStart.isBefore(sEnd) && newEnd.isAfter(sStart);
        });
    }

    @Override
    public boolean existsClientConflict(String clientId, LocalDate trainingDate, LocalTime startTime, LocalDateTime endTime, String excludeSessionId) {
        if (clientId == null || trainingDate == null || startTime == null) {
            return false;
        }

        LocalDateTime newStart = LocalDateTime.of(trainingDate, startTime);
        LocalDateTime newEnd = (endTime != null && endTime.isAfter(newStart)) ? endTime : newStart.plusHours(1);

        List<Criteria> criteriaList = new ArrayList<>();
        criteriaList.add(Criteria.where("clientId").is(clientId.trim()));
        criteriaList.add(Criteria.where("trainingDate").is(trainingDate));
        criteriaList.add(Criteria.where("status").ne(TrainingSessionStatus.CANCELLED));

        if (excludeSessionId != null && !excludeSessionId.isBlank()) {
            criteriaList.add(Criteria.where("id").ne(excludeSessionId.trim()));
        }

        Query query = new Query(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        List<TrainingSessionDocument> existingSessions = mongoTemplate.find(query, TrainingSessionDocument.class);

        return existingSessions.stream().anyMatch(s -> {
            LocalDateTime sStart = LocalDateTime.of(s.getTrainingDate(), s.getStartTime());
            LocalDateTime sEnd = (s.getEndTime() != null && s.getEndTime().isAfter(sStart)) ? s.getEndTime() : sStart.plusHours(1);
            return newStart.isBefore(sEnd) && newEnd.isAfter(sStart);
        });
    }

    @Override
    public List<TrainingSessionEntity> findSessionsForCalendar(
            String trainerId,
            String clientId,
            String contractId,
            TrainingSessionStatus status,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Query query = buildFilterQuery(trainerId, clientId, contractId, status, startDate, endDate);
        query.with(Sort.by(Sort.Direction.ASC, "trainingDate").and(Sort.by(Sort.Direction.ASC, "startTime")));

        return mongoTemplate.find(query, TrainingSessionDocument.class)
                .stream()
                .map(TrainingSessionMapper::toEntity)
                .toList();
    }

    @Override
    public List<TrainingSessionEntity> findSessionsWithFilter(
            String trainerId,
            String clientId,
            String contractId,
            TrainingSessionStatus status,
            LocalDate startDate,
            LocalDate endDate,
            int page,
            int size
    ) {
        Query query = buildFilterQuery(trainerId, clientId, contractId, status, startDate, endDate);
        int validPage = Math.max(1, page);
        int validSize = Math.max(1, size);
        query.skip((long) (validPage - 1) * validSize);
        query.limit(validSize);
        query.with(Sort.by(Sort.Direction.DESC, "trainingDate").and(Sort.by(Sort.Direction.DESC, "startTime")));

        return mongoTemplate.find(query, TrainingSessionDocument.class)
                .stream()
                .map(TrainingSessionMapper::toEntity)
                .toList();
    }

    @Override
    public long countSessionsWithFilter(
            String trainerId,
            String clientId,
            String contractId,
            TrainingSessionStatus status,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Query query = buildFilterQuery(trainerId, clientId, contractId, status, startDate, endDate);
        return mongoTemplate.count(query, TrainingSessionDocument.class);
    }

    @Override
    public List<TrainingSessionEntity> findByTrainerId(String trainerId) {
        return mongoRepository.findByTrainerId(trainerId)
                .stream()
                .map(TrainingSessionMapper::toEntity)
                .toList();
    }

    @Override
    public List<TrainingSessionEntity> findByClientId(String clientId) {
        return mongoRepository.findByClientId(clientId)
                .stream()
                .map(TrainingSessionMapper::toEntity)
                .toList();
    }

    @Override
    public List<TrainingSessionEntity> findByContractId(String contractId) {
        return mongoRepository.findByContractId(contractId)
                .stream()
                .map(TrainingSessionMapper::toEntity)
                .toList();
    }

    private Query buildFilterQuery(
            String trainerId,
            String clientId,
            String contractId,
            TrainingSessionStatus status,
            LocalDate startDate,
            LocalDate endDate
    ) {
        List<Criteria> criteriaList = new ArrayList<>();

        if (trainerId != null && !trainerId.isBlank()) {
            criteriaList.add(Criteria.where("trainerId").is(trainerId.trim()));
        }
        if (clientId != null && !clientId.isBlank()) {
            criteriaList.add(Criteria.where("clientId").is(clientId.trim()));
        }
        if (contractId != null && !contractId.isBlank()) {
            criteriaList.add(Criteria.where("contractId").is(contractId.trim()));
        }
        if (status != null) {
            criteriaList.add(Criteria.where("status").is(status));
        }

        if (startDate != null && endDate != null) {
            criteriaList.add(Criteria.where("trainingDate").gte(startDate).lte(endDate));
        } else if (startDate != null) {
            criteriaList.add(Criteria.where("trainingDate").gte(startDate));
        } else if (endDate != null) {
            criteriaList.add(Criteria.where("trainingDate").lte(endDate));
        }

        Query query = new Query();
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }
        return query;
    }
}

