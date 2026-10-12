package x10.zenfit.mvp.service.infrastructure.order.adapter;

import x10.zenfit.mvp.service.common.domain.entities.order.ShippingRuleEntity;
import x10.zenfit.mvp.service.infrastructure.order.db.mongodb.ShippingRuleDocument;

public class ShippingRuleMapper {

    private ShippingRuleMapper() {
    }

    public static ShippingRuleEntity toEntity(ShippingRuleDocument document) {
        if (document == null) return null;
        return ShippingRuleEntity.builder()
                .id(document.getId())
                .name(document.getName())
                .defaultShippingFee(document.getDefaultShippingFee())
                .freeShippingThreshold(document.getFreeShippingThreshold())
                .freeShippingDates(document.getFreeShippingDates())
                .active(document.getActive())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }

    public static ShippingRuleDocument toDocument(ShippingRuleEntity entity) {
        if (entity == null) return null;
        return ShippingRuleDocument.builder()
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
