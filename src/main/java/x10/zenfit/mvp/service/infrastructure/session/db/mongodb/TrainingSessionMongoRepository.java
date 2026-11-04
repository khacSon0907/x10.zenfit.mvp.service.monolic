package x10.zenfit.mvp.service.infrastructure.session.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TrainingSessionMongoRepository extends MongoRepository<TrainingSessionDocument, String> {

    List<TrainingSessionDocument> findByTrainerId(String trainerId);

    List<TrainingSessionDocument> findByClientId(String clientId);

    List<TrainingSessionDocument> findByContractId(String contractId);
}

