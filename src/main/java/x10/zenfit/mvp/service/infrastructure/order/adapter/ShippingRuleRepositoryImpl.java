package x10.zenfit.mvp.service.infrastructure.order.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.Repository.IShippingRuleRepository;
import x10.zenfit.mvp.service.common.domain.entities.order.ShippingRuleEntity;
import x10.zenfit.mvp.service.infrastructure.order.db.mongodb.ShippingRuleMongoRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ShippingRuleRepositoryImpl implements IShippingRuleRepository {

    private final ShippingRuleMongoRepository shippingRuleMongoRepository;

    @Override
    public ShippingRuleEntity save(ShippingRuleEntity shippingRule) {
        var document = ShippingRuleMapper.toDocument(shippingRule);
        var saved = shippingRuleMongoRepository.save(document);
        return ShippingRuleMapper.toEntity(saved);
    }

    @Override
    public List<ShippingRuleEntity> saveAll(List<ShippingRuleEntity> shippingRules) {
        return shippingRuleMongoRepository.saveAll(
                        shippingRules.stream()
                                .map(ShippingRuleMapper::toDocument)
                                .toList()
                )
                .stream()
                .map(ShippingRuleMapper::toEntity)
                .toList();
    }

    @Override
    public Optional<ShippingRuleEntity> findById(String id) {
        return shippingRuleMongoRepository.findById(id)
                .map(ShippingRuleMapper::toEntity);
    }

    @Override
    public Optional<ShippingRuleEntity> findActiveRule() {
        return shippingRuleMongoRepository.findFirstByActiveTrueOrderByUpdatedAtDesc()
                .map(ShippingRuleMapper::toEntity);
    }

    @Override
    public List<ShippingRuleEntity> findAll() {
        return shippingRuleMongoRepository.findAll()
                .stream()
                .map(ShippingRuleMapper::toEntity)
                .toList();
    }

    @Override
    public List<ShippingRuleEntity> findAllActive() {
        return shippingRuleMongoRepository.findByActiveTrue()
                .stream()
                .map(ShippingRuleMapper::toEntity)
                .toList();
    }

    @Override
    public void deleteById(String id) {
        shippingRuleMongoRepository.deleteById(id);
    }
}
