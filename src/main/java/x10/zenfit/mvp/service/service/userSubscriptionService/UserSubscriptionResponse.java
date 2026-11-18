package x10.zenfit.mvp.service.service.userSubscriptionService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.entities.subscription.SubscriptionPlanEntity;
import x10.zenfit.mvp.service.common.domain.entities.subscription.UserSubscriptionEntity;
import x10.zenfit.mvp.service.common.domain.enums.UserSubscriptionStatus;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSubscriptionResponse {

    private String id;
    private String userId;
    private String planId;
    private String planName;
    private String planCode;

    private UserSubscriptionStatus status;
    private Instant startAt;
    private Instant endAt;

    private BigDecimal price;
    private Integer maxStudents;
    private Integer currentStudents;

    private Boolean autoRenew;
    private Instant paidAt;
    private String transactionId;

    private Long payosOrderCode;
    private String checkoutUrl;
    private String qrCode;

    private boolean isExpired;
    private Long daysRemaining;

    private Instant createdAt;
    private Instant updatedAt;

    public static UserSubscriptionResponse fromEntity(UserSubscriptionEntity entity, SubscriptionPlanEntity plan) {
        if (entity == null) {
            return null;
        }

        Instant now = Instant.now();
        boolean expired = entity.getEndAt() != null && now.isAfter(entity.getEndAt());
        Long days = null;
        if (entity.getEndAt() != null) {
            long remaining = Duration.between(now, entity.getEndAt()).toDays();
            days = remaining > 0 ? remaining : 0L;
        }

        return UserSubscriptionResponse.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .planId(entity.getPlanId())
                .planName(plan != null ? plan.getName() : null)
                .planCode(plan != null ? plan.getCode() : null)
                .status(entity.getStatus())
                .startAt(entity.getStartAt())
                .endAt(entity.getEndAt())
                .price(entity.getPrice())
                .maxStudents(entity.getMaxStudents())
                .currentStudents(entity.getCurrentStudents() != null ? entity.getCurrentStudents() : 0)
                .autoRenew(entity.getAutoRenew())
                .paidAt(entity.getPaidAt())
                .transactionId(entity.getTransactionId())
                .payosOrderCode(entity.getPayosOrderCode())
                .checkoutUrl(entity.getCheckoutUrl())
                .qrCode(entity.getQrCode())
                .isExpired(expired)
                .daysRemaining(days)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

