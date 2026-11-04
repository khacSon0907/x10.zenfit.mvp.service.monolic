package x10.zenfit.mvp.service.infrastructure.session.adapter.mapper;

import x10.zenfit.mvp.service.common.domain.entities.session.TrainingSessionEntity;
import x10.zenfit.mvp.service.infrastructure.session.db.mongodb.TrainingSessionDocument;

public final class TrainingSessionMapper {

    private TrainingSessionMapper() {
    }

    public static TrainingSessionEntity toEntity(TrainingSessionDocument document) {
        if (document == null) {
            return null;
        }

        return TrainingSessionEntity.builder()
                .id(document.getId())
                .clientId(document.getClientId())
                .trainerId(document.getTrainerId())
                .contractId(document.getContractId())
                .sessionName(document.getSessionName())
                .trainingDate(document.getTrainingDate())
                .startTime(document.getStartTime())
                .endTime(document.getEndTime())
                .status(document.getStatus())
                .checkInAt(document.getCheckInAt())
                .completedAt(document.getCompletedAt())
                .evidenceImageUrls(document.getEvidenceImageUrls())
                .note(document.getNote())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }

    public static TrainingSessionDocument toDocument(TrainingSessionEntity entity) {
        if (entity == null) {
            return null;
        }

        return TrainingSessionDocument.builder()
                .id(entity.getId())
                .clientId(entity.getClientId())
                .trainerId(entity.getTrainerId())
                .contractId(entity.getContractId())
                .sessionName(entity.getSessionName())
                .trainingDate(entity.getTrainingDate())
                .startTime(entity.getStartTime())
                .endTime(entity.getEndTime())
                .status(entity.getStatus())
                .checkInAt(entity.getCheckInAt())
                .completedAt(entity.getCompletedAt())
                .evidenceImageUrls(entity.getEvidenceImageUrls())
                .note(entity.getNote())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

