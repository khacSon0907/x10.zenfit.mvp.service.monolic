package x10.zenfit.mvp.service.service.userSubscriptionService.selectPlanUc;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SelectPlanRequest {

    @NotBlank(message = "Mã ID gói dịch vụ không được để trống")
    private String planId;

    private Boolean autoRenew;
}

