package x10.zenfit.mvp.service.infrastructure.revenue.adapter;

import x10.zenfit.mvp.service.common.domain.entities.revenue.RevenueTargetEntity;
import x10.zenfit.mvp.service.infrastructure.revenue.db.mongodb.RevenueTargetDocument;

public class RevenueTargetMapper {

    private RevenueTargetMapper() {
    }

    public static RevenueTargetEntity toEntity(RevenueTargetDocument document) {
        if (document == null) {
            return null;
        }

        return RevenueTargetEntity.builder()
                .id(document.getId())
                .year(document.getYear())
                .month(document.getMonth())
                .targetRevenue(document.getTargetRevenue())
                .targetOrders(document.getTargetOrders())
                .note(document.getNote())
                .createdBy(document.getCreatedBy())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }
}
