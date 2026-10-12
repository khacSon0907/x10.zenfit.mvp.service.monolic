package x10.zenfit.mvp.service.Repository;

import x10.zenfit.mvp.service.common.domain.entities.point.PointEntity;

import java.util.List;
import java.util.Optional;

public interface IPointRepository {
    PointEntity save(PointEntity pointRule);
    List<PointEntity> saveAll(List<PointEntity> pointRules);
    Optional<PointEntity> findById(String id);
    Optional<PointEntity> findActiveRule();
    List<PointEntity> findAll();
    List<PointEntity> findAllActive();
    void deleteById(String id);
}
