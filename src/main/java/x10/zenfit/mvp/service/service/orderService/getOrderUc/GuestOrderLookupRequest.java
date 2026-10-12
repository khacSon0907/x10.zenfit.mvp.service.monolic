package x10.zenfit.mvp.service.service.orderService.getOrderUc;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GuestOrderLookupRequest {

    @NotBlank(message = "Ma don hang khong duoc de trong")
    private String orderCode;

    private String email;

    private String receiverPhone;
}
