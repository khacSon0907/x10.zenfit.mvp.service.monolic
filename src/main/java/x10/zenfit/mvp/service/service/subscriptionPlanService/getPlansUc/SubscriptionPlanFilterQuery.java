package x10.zenfit.mvp.service.service.subscriptionPlanService.getPlansUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.SubscriptionPlanStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionPlanFilterQuery {

    private String keyword;
    private SubscriptionPlanStatus status;
    private Integer page;
    private Integer size;
}

