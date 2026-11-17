package x10.zenfit.mvp.service.service.subscriptionPlanService.updatePlanStatusUc;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.SubscriptionPlanStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSubscriptionPlanStatusRequest {

    @NotNull(message = "Trạng thái không được để trống")
    private SubscriptionPlanStatus status;
}

