package x10.zenfit.mvp.service.service.orderService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IShippingRuleRepository;
import x10.zenfit.mvp.service.common.domain.entities.order.ShippingRuleEntity;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.order.ShippingRuleError;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CoreShippingRuleServiceImpl implements ICoreShippingRuleService {

    private final IShippingRuleRepository shippingRuleRepository;

    @Override
    public ShippingRuleResponse createRule(ShippingRuleRequest request) {
        Instant now = Instant.now();
        boolean active = request.getActive() == null || request.getActive();

        if (active) {
            deactivateActiveRules(null, now);
        }

        ShippingRuleEntity entity = ShippingRuleEntity.builder()
                .id(UUID.randomUUID().toString())
                .name(request.getName().trim())
                .defaultShippingFee(defaultZero(request.getDefaultShippingFee()))
                .freeShippingThreshold(request.getFreeShippingThreshold())
                .freeShippingDates(normalizeDates(request.getFreeShippingDates()))
                .active(active)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return ShippingRuleResponseMapper.toResponse(shippingRuleRepository.save(entity));
    }

    @Override
    public ShippingRuleResponse updateRule(String id, ShippingRuleRequest request) {
        ShippingRuleEntity existing = shippingRuleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ShippingRuleError.SHIPPING_RULE_NOT_FOUND));

        Instant now = Instant.now();
        boolean active = request.getActive() == null || request.getActive();

        if (active) {
            deactivateActiveRules(id, now);
        }

        existing.setName(request.getName().trim());
        existing.setDefaultShippingFee(defaultZero(request.getDefaultShippingFee()));
        existing.setFreeShippingThreshold(request.getFreeShippingThreshold());
        existing.setFreeShippingDates(normalizeDates(request.getFreeShippingDates()));
        existing.setActive(active);
        existing.setUpdatedAt(now);

        return ShippingRuleResponseMapper.toResponse(shippingRuleRepository.save(existing));
    }

    @Override
    public List<ShippingRuleResponse> getAllRules() {
        return shippingRuleRepository.findAll()
                .stream()
                .map(ShippingRuleResponseMapper::toResponse)
                .toList();
    }

    @Override
    public ShippingRuleResponse getActiveRule() {
        return shippingRuleRepository.findActiveRule()
                .map(ShippingRuleResponseMapper::toResponse)
                .orElse(null);
    }

    @Override
    public void deleteRule(String id) {
        ShippingRuleEntity existing = shippingRuleRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ShippingRuleError.SHIPPING_RULE_NOT_FOUND));

        shippingRuleRepository.deleteById(existing.getId());
    }

    @Override
    public BigDecimal calculateShippingFee(BigDecimal subtotal, LocalDate orderDate) {
        return shippingRuleRepository.findActiveRule()
                .map(rule -> calculateFromRule(rule, defaultZero(subtotal), orderDate))
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal calculateFromRule(ShippingRuleEntity rule, BigDecimal subtotal, LocalDate orderDate) {
        if (isFreeShippingDate(rule, orderDate)) {
            return BigDecimal.ZERO;
        }

        BigDecimal threshold = rule.getFreeShippingThreshold();
        if (threshold != null && subtotal.compareTo(threshold) >= 0) {
            return BigDecimal.ZERO;
        }

        return defaultZero(rule.getDefaultShippingFee());
    }

    private boolean isFreeShippingDate(ShippingRuleEntity rule, LocalDate orderDate) {
        if (orderDate == null || rule.getFreeShippingDates() == null) {
            return false;
        }

        return rule.getFreeShippingDates().contains(orderDate);
    }

    private void deactivateActiveRules(String keepId, Instant now) {
        List<ShippingRuleEntity> activeRules = shippingRuleRepository.findAllActive()
                .stream()
                .filter(rule -> keepId == null || !keepId.equals(rule.getId()))
                .peek(rule -> {
                    rule.setActive(false);
                    rule.setUpdatedAt(now);
                })
                .toList();

        if (!activeRules.isEmpty()) {
            shippingRuleRepository.saveAll(activeRules);
        }
    }

    private List<LocalDate> normalizeDates(List<LocalDate> dates) {
        if (dates == null) {
            return new ArrayList<>();
        }

        return dates.stream()
                .distinct()
                .sorted()
                .toList();
    }

    private BigDecimal defaultZero(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }
}
