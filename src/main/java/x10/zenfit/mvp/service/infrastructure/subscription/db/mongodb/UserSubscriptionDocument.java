package x10.zenfit.mvp.service.infrastructure.subscription.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import x10.zenfit.mvp.service.common.domain.enums.UserSubscriptionStatus;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "user_subscriptions")
public class UserSubscriptionDocument {

    @Id
    private String id;

    @Indexed
    private String userId;

    @Indexed
    private String planId;

    @Indexed
    private UserSubscriptionStatus status;

    private Instant startAt;
    private Instant endAt;

    private BigDecimal price;
    private Integer maxStudents;
    private Integer currentStudents;

    private Boolean autoRenew;
    private Instant paidAt;
    private String transactionId;

    private Instant createdAt;
    private Instant updatedAt;
}

