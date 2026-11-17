package x10.zenfit.mvp.service.service.subscriptionPlanService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.entities.subscription.SubscriptionPlanEntity;
import x10.zenfit.mvp.service.common.domain.enums.SubscriptionPlanStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlanResponse {

    private String id;
    private String code;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer durationDays;
    private Integer maxStudents;
    private Integer maxTrainingSessions;
    private SubscriptionPlanStatus status;
    private Boolean isDefault;
    private Integer displayOrder;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SubscriptionPlanResponse fromEntity(SubscriptionPlanEntity entity) {
        if (entity == null) {
            return null;
        }

        return SubscriptionPlanResponse.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .durationDays(entity.getDurationDays())
                .maxStudents(entity.getMaxStudents())
                .maxTrainingSessions(entity.getMaxTrainingSessions())
                .status(entity.getStatus())
                .isDefault(entity.getIsDefault())
                .displayOrder(entity.getDisplayOrder())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

