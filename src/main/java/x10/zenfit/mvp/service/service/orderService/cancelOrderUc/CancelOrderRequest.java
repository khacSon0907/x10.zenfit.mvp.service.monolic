package x10.zenfit.mvp.service.service.orderService.cancelOrderUc;

import jakarta.validation.constraints.Size;
import lombok.Data;
import x10.zenfit.mvp.service.common.domain.enums.CancelReasonType;

@Data
public class CancelOrderRequest {
    private CancelReasonType reasonType;

    @Size(max = 500, message = "Ly do huy don khong duoc vuot qua 500 ky tu")
    private String reasonDetail;
}
