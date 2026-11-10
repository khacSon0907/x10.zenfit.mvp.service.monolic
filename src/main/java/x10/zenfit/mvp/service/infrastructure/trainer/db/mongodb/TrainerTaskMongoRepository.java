package x10.zenfit.mvp.service.infrastructure.trainer.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TrainerTaskMongoRepository extends MongoRepository<TrainerTaskDocument, String> {

    List<TrainerTaskDocument> findByTrainerIdAndTaskDate(String trainerId, LocalDate taskDate);

    List<TrainerTaskDocument> findByTrainerIdAndTaskDateBetween(String trainerId, LocalDate startDate, LocalDate endDate);
}

