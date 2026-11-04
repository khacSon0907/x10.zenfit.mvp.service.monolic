package x10.zenfit.mvp.service.Repository;

import x10.zenfit.mvp.service.common.domain.entities.session.TrainingSessionEntity;
import x10.zenfit.mvp.service.common.domain.enums.TrainingSessionStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface ITrainingSessionRepository {

    TrainingSessionEntity save(TrainingSessionEntity session);

    Optional<TrainingSessionEntity> findById(String id);

    boolean existsById(String id);

    boolean existsTrainerConflict(String trainerId, LocalDate trainingDate, LocalTime startTime, LocalDateTime endTime, String excludeSessionId);

    default boolean existsTrainerConflict(String trainerId, LocalDate trainingDate, LocalTime startTime, String excludeSessionId) {
        return existsTrainerConflict(trainerId, trainingDate, startTime, null, excludeSessionId);
    }

    boolean existsClientConflict(String clientId, LocalDate trainingDate, LocalTime startTime, LocalDateTime endTime, String excludeSessionId);

    default boolean existsClientConflict(String clientId, LocalDate trainingDate, LocalTime startTime, String excludeSessionId) {
        return existsClientConflict(clientId, trainingDate, startTime, null, excludeSessionId);
    }

    List<TrainingSessionEntity> findSessionsForCalendar(
            String trainerId,
            String clientId,
            String contractId,
            TrainingSessionStatus status,
            LocalDate startDate,
            LocalDate endDate
    );

    List<TrainingSessionEntity> findSessionsWithFilter(
            String trainerId,
            String clientId,
            String contractId,
            TrainingSessionStatus status,
            LocalDate startDate,
            LocalDate endDate,
            int page,
            int size
    );

    long countSessionsWithFilter(
            String trainerId,
            String clientId,
            String contractId,
            TrainingSessionStatus status,
            LocalDate startDate,
            LocalDate endDate
    );

    List<TrainingSessionEntity> findByTrainerId(String trainerId);

    List<TrainingSessionEntity> findByClientId(String clientId);

    List<TrainingSessionEntity> findByContractId(String contractId);
}

