package x10.zenfit.mvp.service.service.contractService;

import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.common.domain.entities.contract.PtContractEntity;
import x10.zenfit.mvp.service.service.clientService.ClientResponse;
import x10.zenfit.mvp.service.service.clientService.ClientResponseMapper;

import java.math.BigDecimal;

public final class PtContractResponseMapper {

    private PtContractResponseMapper() {
    }

    public static PtContractResponse toResponse(PtContractEntity entity) {
        return toResponse(entity, (ClientResponse) null);
    }

    public static PtContractResponse toResponse(PtContractEntity entity, ClientEntity clientEntity) {
        ClientResponse clientResponse = clientEntity != null ? ClientResponseMapper.toResponse(clientEntity) : null;
        return toResponse(entity, clientResponse);
    }

    public static PtContractResponse toResponse(PtContractEntity entity, ClientResponse clientResponse) {
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
                .goal(entity.getGoal())
                .targetWeight(entity.getTargetWeight())
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
