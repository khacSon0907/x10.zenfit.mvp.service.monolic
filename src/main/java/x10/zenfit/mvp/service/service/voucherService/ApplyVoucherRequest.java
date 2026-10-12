package x10.zenfit.mvp.service.service.voucherService;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ApplyVoucherRequest {
    @NotBlank(message = "Ma voucher khong duoc de trong")
    private String code;

    private String userId;

    @NotNull(message = "Tam tinh don hang khong duoc de trong")
    @DecimalMin(value = "0.0", message = "Tam tinh don hang khong duoc am")
    private BigDecimal subtotal;
}
