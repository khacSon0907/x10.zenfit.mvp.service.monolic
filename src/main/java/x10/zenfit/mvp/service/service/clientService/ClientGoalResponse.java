package x10.zenfit.mvp.service.service.clientService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientGoalEntity;
import x10.zenfit.mvp.service.common.domain.enums.GoalPhaseStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientGoalResponse {

    private String id;
    private String clientId;
    private String goalType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double startWeight;
    private Double targetWeight;
    private Double endWeight;
    private GoalPhaseStatus status;
    private String note;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ClientGoalResponse fromEntity(ClientGoalEntity entity) {
        if (entity == null) {
            return null;
        }

        return ClientGoalResponse.builder()
                .id(entity.getId())
                .clientId(entity.getClientId())
                .goalType(entity.getGoalType())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
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
