package x10.zenfit.mvp.service.service.userSubscriptionService.getAdminSubscriptionsUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.UserSubscriptionStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSubscriptionFilterQuery {

    private String userId;
    private String planId;
    private UserSubscriptionStatus status;
    private Integer page;
    private Integer size;
}

