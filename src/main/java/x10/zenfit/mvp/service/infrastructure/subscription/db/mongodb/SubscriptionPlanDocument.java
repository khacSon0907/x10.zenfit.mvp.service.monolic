package x10.zenfit.mvp.service.infrastructure.subscription.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import x10.zenfit.mvp.service.common.domain.enums.SubscriptionPlanStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "subscription_plans")
public class SubscriptionPlanDocument {

    @Id
    private String id;

    @Indexed(unique = true)
    private String code;

    private String name;
    private String description;

    private BigDecimal price;
    private Integer durationDays;

    private Integer maxStudents;
    private Integer maxTrainingSessions;

    private SubscriptionPlanStatus status;

    @Indexed
    private Boolean isDefault;

    private Integer displayOrder;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

