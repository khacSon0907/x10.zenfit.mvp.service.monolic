package x10.zenfit.mvp.service.infrastructure.revenue.db.mongodb;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RevenueTargetMongoRepository extends MongoRepository<RevenueTargetDocument, String> {
    Optional<RevenueTargetDocument> findByYearAndMonth(Integer year, Integer month);
    List<RevenueTargetDocument> findAllByOrderByYearDescMonthDesc();
}
