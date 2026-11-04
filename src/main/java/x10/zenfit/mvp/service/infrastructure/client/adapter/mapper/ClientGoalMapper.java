package x10.zenfit.mvp.service.infrastructure.client.adapter.mapper;

import x10.zenfit.mvp.service.common.domain.entities.client.ClientGoalEntity;
import x10.zenfit.mvp.service.infrastructure.client.db.mongodb.ClientGoalDocument;

public final class ClientGoalMapper {

    private ClientGoalMapper() {
    }

    public static ClientGoalEntity toEntity(ClientGoalDocument document) {
        if (document == null) {
            return null;
        }

        return ClientGoalEntity.builder()
                .id(document.getId())
                .clientId(document.getClientId())
                .trainerId(document.getTrainerId())
                .goalName(document.getGoalName())
                .goalType(document.getGoalType())
                .startDate(document.getStartDate())
                .endDate(document.getEndDate())
                .actualEndDate(document.getActualEndDate())
                .startWeight(document.getStartWeight())
                .targetWeight(document.getTargetWeight())
                .endWeight(document.getEndWeight())
                .status(document.getStatus())
                .note(document.getNote())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }

    public static ClientGoalDocument toDocument(ClientGoalEntity entity) {
        if (entity == null) {
            return null;
        }

        return ClientGoalDocument.builder()
                .id(entity.getId())
                .clientId(entity.getClientId())
                .trainerId(entity.getTrainerId())
                .goalName(entity.getGoalName())
                .goalType(entity.getGoalType())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .actualEndDate(entity.getActualEndDate())
                .startWeight(entity.getStartWeight())
                .targetWeight(entity.getTargetWeight())
                .endWeight(entity.getEndWeight())
                .status(entity.getStatus())
                .note(entity.getNote())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
