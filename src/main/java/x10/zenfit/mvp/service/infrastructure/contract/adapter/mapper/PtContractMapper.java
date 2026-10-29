package x10.zenfit.mvp.service.infrastructure.contract.adapter.mapper;

import x10.zenfit.mvp.service.common.domain.entities.contract.PtContractEntity;
import x10.zenfit.mvp.service.infrastructure.contract.db.mongodb.PtContractDocument;

public final class PtContractMapper {

    private PtContractMapper() {
    }

    public static PtContractEntity toEntity(PtContractDocument document) {
        if (document == null) {
            return null;
        }

        return PtContractEntity.builder()
                .id(document.getId())
                .clientId(document.getClientId())
                .trainerId(document.getTrainerId())
                .totalSessions(document.getTotalSessions())
                .bonusSessions(document.getBonusSessions())
                .usedSessions(document.getUsedSessions())
                .startDate(document.getStartDate())
                .endDate(document.getEndDate())
                .price(document.getPrice())
                .paidAmount(document.getPaidAmount())
                .status(document.getStatus())
                .notes(document.getNotes())
                .createdAt(document.getCreatedAt())
                .updatedAt(document.getUpdatedAt())
                .build();
    }

    public static PtContractDocument toDocument(PtContractEntity entity) {
        if (entity == null) {
            return null;
        }

        return PtContractDocument.builder()
                .id(entity.getId())
                .clientId(entity.getClientId())
                .trainerId(entity.getTrainerId())
                .totalSessions(entity.getTotalSessions())
                .bonusSessions(entity.getBonusSessions())
                .usedSessions(entity.getUsedSessions())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .price(entity.getPrice())
                .paidAmount(entity.getPaidAmount())
                .status(entity.getStatus())
                .notes(entity.getNotes())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
