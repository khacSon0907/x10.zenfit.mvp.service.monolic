package x10.zenfit.mvp.service.Repository;

import x10.zenfit.mvp.service.common.domain.entities.trainer.TrainerTaskEntity;
import x10.zenfit.mvp.service.common.domain.enums.TrainerTaskStatus;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ITrainerTaskRepository {

    TrainerTaskEntity save(TrainerTaskEntity task);

    Optional<TrainerTaskEntity> findById(String id);

    boolean existsById(String id);

    void deleteById(String id);

    List<TrainerTaskEntity> findTasks(
            String trainerId,
            LocalDate taskDate,
            LocalDate startDate,
            LocalDate endDate,
            TrainerTaskStatus status
    );
}

