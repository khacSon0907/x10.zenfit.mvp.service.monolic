package x10.zenfit.mvp.service.service.orderService;

import x10.zenfit.mvp.service.common.domain.entities.order.ShippingRuleEntity;

public class ShippingRuleResponseMapper {

    private ShippingRuleResponseMapper() {
    }

    public static ShippingRuleResponse toResponse(ShippingRuleEntity entity) {
        if (entity == null) return null;
        return ShippingRuleResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .defaultShippingFee(entity.getDefaultShippingFee())
                .freeShippingThreshold(entity.getFreeShippingThreshold())
                .freeShippingDates(entity.getFreeShippingDates())
                .active(entity.getActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
