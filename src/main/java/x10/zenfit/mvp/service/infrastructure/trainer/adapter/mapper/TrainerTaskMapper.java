package x10.zenfit.mvp.service.infrastructure.trainer.adapter.mapper;

import x10.zenfit.mvp.service.common.domain.entities.trainer.TrainerTaskEntity;
import x10.zenfit.mvp.service.infrastructure.trainer.db.mongodb.TrainerTaskDocument;

public final class TrainerTaskMapper {

    private TrainerTaskMapper() {
    }

    public static TrainerTaskEntity toEntity(TrainerTaskDocument document) {
        if (document == null) {
            return null;
        }

        return TrainerTaskEntity.builder()
                .id(document.getId())
                .trainerId(document.getTrainerId())
                .title(document.getTitle())
                .description(document.getDescription())
                .color(document.getColor())
                .taskDate(document.getTaskDate())
                .startTime(document.getStartTime())
                .endTime(document.getEndTime())
                .allDay(document.isAllDay())
                .status(document.getStatus())
                .completedAt(document.getCompletedAt())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }

    public static TrainerTaskDocument toDocument(TrainerTaskEntity entity) {
        if (entity == null) {
            return null;
        }

        return TrainerTaskDocument.builder()
                .id(entity.getId())
                .trainerId(entity.getTrainerId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .color(entity.getColor())
                .taskDate(entity.getTaskDate())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .allDay(entity.isAllDay())
                .status(entity.getStatus())
                .completedAt(entity.getCompletedAt())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

