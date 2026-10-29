package x10.zenfit.mvp.service.service.contractService;

import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientGoalEntity;
import x10.zenfit.mvp.service.common.domain.entities.contract.PtContractEntity;
import x10.zenfit.mvp.service.service.clientService.ClientGoalResponse;
import x10.zenfit.mvp.service.service.clientService.ClientResponse;
import x10.zenfit.mvp.service.service.clientService.ClientResponseMapper;

import java.math.BigDecimal;

public final class PtContractResponseMapper {

    private PtContractResponseMapper() {
    }

    public static PtContractResponse toResponse(PtContractEntity entity) {
        return toResponse(entity, (ClientResponse) null, (ClientGoalResponse) null);
    }

    public static PtContractResponse toResponse(PtContractEntity entity, ClientEntity clientEntity) {
        return toResponse(entity, clientEntity, null);
    }

    public static PtContractResponse toResponse(PtContractEntity entity, ClientEntity clientEntity, ClientGoalEntity goalEntity) {
        ClientResponse clientResponse = clientEntity != null ? ClientResponseMapper.toResponse(clientEntity) : null;
        ClientGoalResponse goalResponse = goalEntity != null ? ClientGoalResponse.fromEntity(goalEntity) : null;
        return toResponse(entity, clientResponse, goalResponse);
    }

    public static PtContractResponse toResponse(PtContractEntity entity, ClientResponse clientResponse) {
        return toResponse(entity, clientResponse, null);
    }

    public static PtContractResponse toResponse(PtContractEntity entity, ClientResponse clientResponse, ClientGoalResponse goalResponse) {
        if (entity == null) {
            return null;
        }

        BigDecimal price = entity.getPrice() != null ? entity.getPrice() : BigDecimal.ZERO;
        BigDecimal paid = entity.getPaidAmount() != null ? entity.getPaidAmount() : BigDecimal.ZERO;
        BigDecimal remainingAmount = price.subtract(paid);

        return PtContractResponse.builder()
                .id(entity.getId())
                .clientId(entity.getClientId())
                .trainerId(entity.getTrainerId())
                .client(clientResponse)
                .goal(goalResponse)
                .totalSessions(entity.getTotalSessions())
                .bonusSessions(entity.getBonusSessions())
                .usedSessions(entity.getUsedSessions())
                .remainingSessions(entity.getRemainingSessions())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .price(entity.getPrice())
                .paidAmount(entity.getPaidAmount())
                .remainingAmount(remainingAmount)
                .status(entity.getStatus())
                .notes(entity.getNotes())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}
