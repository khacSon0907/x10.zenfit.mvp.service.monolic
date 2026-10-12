package x10.zenfit.mvp.service.Repository;

import x10.zenfit.mvp.service.common.domain.entities.order.ShippingRuleEntity;

import java.util.List;
import java.util.Optional;

public interface IShippingRuleRepository {
    ShippingRuleEntity save(ShippingRuleEntity shippingRule);
    List<ShippingRuleEntity> saveAll(List<ShippingRuleEntity> shippingRules);
    Optional<ShippingRuleEntity> findById(String id);
    Optional<ShippingRuleEntity> findActiveRule();
    List<ShippingRuleEntity> findAll();
    List<ShippingRuleEntity> findAllActive();
    void deleteById(String id);
}
