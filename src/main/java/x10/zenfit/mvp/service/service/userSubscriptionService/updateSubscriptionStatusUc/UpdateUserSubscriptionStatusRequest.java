package x10.zenfit.mvp.service.service.userSubscriptionService.updateSubscriptionStatusUc;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.UserSubscriptionStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserSubscriptionStatusRequest {

    @NotNull(message = "Trạng thái không được để trống")
    private UserSubscriptionStatus status;
}

