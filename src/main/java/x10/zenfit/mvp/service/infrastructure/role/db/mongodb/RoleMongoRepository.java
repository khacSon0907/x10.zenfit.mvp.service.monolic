package x10.zenfit.mvp.service.infrastructure.role.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleMongoRepository extends MongoRepository<RoleDocument, String> {

    Optional<RoleDocument> findByCode(String code);

    boolean existsByCode(String code);
}
