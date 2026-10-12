package x10.zenfit.mvp.service.infrastructure.point.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface PointMongoRepository extends MongoRepository<PointDocument, String> {
    Optional<PointDocument> findFirstByActiveTrueOrderByUpdatedAtDesc();
    List<PointDocument> findByActiveTrue();
}
