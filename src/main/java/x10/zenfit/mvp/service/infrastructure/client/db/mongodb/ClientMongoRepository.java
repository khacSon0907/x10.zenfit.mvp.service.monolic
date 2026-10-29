package x10.zenfit.mvp.service.infrastructure.client.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientMongoRepository extends MongoRepository<ClientDocument, String> {

    Optional<ClientDocument> findByEmail(String email);

    Optional<ClientDocument> findByPhone(String phone);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    List<ClientDocument> findByAssignedTrainerId(String assignedTrainerId);

    List<ClientDocument> findByFullNameContainingIgnoreCase(String fullName);

    List<ClientDocument> findByFullNameIgnoreCase(String fullName);

    List<ClientDocument> findByFullNameContainingIgnoreCaseAndAssignedTrainerId(String fullName, String assignedTrainerId);
}
