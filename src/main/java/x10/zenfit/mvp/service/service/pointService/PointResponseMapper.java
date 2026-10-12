package x10.zenfit.mvp.service.service.pointService;

import x10.zenfit.mvp.service.common.domain.entities.point.PointEntity;

public class PointResponseMapper {

    private PointResponseMapper() {
    }

    public static PointResponse toResponse(PointEntity entity) {
        if (entity == null) {
            return null;
        }

        return PointResponse.builder()
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
