package x10.zenfit.mvp.service.service.orderService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ICoreShippingRuleService {
    ShippingRuleResponse createRule(ShippingRuleRequest request);
    ShippingRuleResponse updateRule(String id, ShippingRuleRequest request);
    List<ShippingRuleResponse> getAllRules();
    ShippingRuleResponse getActiveRule();
    void deleteRule(String id);
    BigDecimal calculateShippingFee(BigDecimal subtotal, LocalDate orderDate);
}
