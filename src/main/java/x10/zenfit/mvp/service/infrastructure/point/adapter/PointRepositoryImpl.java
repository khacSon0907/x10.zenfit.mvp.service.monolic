package x10.zenfit.mvp.service.infrastructure.point.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.Repository.IPointRepository;
import x10.zenfit.mvp.service.common.domain.entities.point.PointEntity;
import x10.zenfit.mvp.service.infrastructure.point.db.mongodb.PointMongoRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements IPointRepository {

    private final PointMongoRepository pointMongoRepository;

    @Override
    public PointEntity save(PointEntity pointRule) {
        var document = PointMapper.toDocument(pointRule);
        var saved = pointMongoRepository.save(document);
        return PointMapper.toEntity(saved);
    }

    @Override
    public List<PointEntity> saveAll(List<PointEntity> pointRules) {
        return pointMongoRepository.saveAll(
                        pointRules.stream()
                                .map(PointMapper::toDocument)
                                .toList()
                )
                .stream()
                .map(PointMapper::toEntity)
                .toList();
    }

    @Override
    public Optional<PointEntity> findById(String id) {
        return pointMongoRepository.findById(id)
                .map(PointMapper::toEntity);
    }

    @Override
    public Optional<PointEntity> findActiveRule() {
        return pointMongoRepository.findFirstByActiveTrueOrderByUpdatedAtDesc()
                .map(PointMapper::toEntity);
    }

    @Override
    public List<PointEntity> findAll() {
        return pointMongoRepository.findAll()
                .stream()
                .map(PointMapper::toEntity)
                .toList();
    }

    @Override
    public List<PointEntity> findAllActive() {
        return pointMongoRepository.findByActiveTrue()
                .stream()
                .map(PointMapper::toEntity)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        pointMongoRepository.deleteById(id);
    }
}
