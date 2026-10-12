package x10.zenfit.mvp.service.infrastructure.point.adapter;

import x10.zenfit.mvp.service.common.domain.entities.point.PointEntity;
import x10.zenfit.mvp.service.infrastructure.point.db.mongodb.PointDocument;

public class PointMapper {

    private PointMapper() {
    }

    public static PointEntity toEntity(PointDocument document) {
        if (document == null) {
            return null;
        }

        return PointEntity.builder()
                .id(document.getId())
                .name(document.getName())
                .moneyAmount(document.getMoneyAmount())
                .points(document.getPoints())
                .active(document.getActive())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }

    public static PointDocument toDocument(PointEntity entity) {
        if (entity == null) {
            return null;
        }

        return PointDocument.builder()
                .id(entity.getId())
                .name(entity.getName())
                .moneyAmount(entity.getMoneyAmount())
                .points(entity.getPoints())
                .active(entity.getActive())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
