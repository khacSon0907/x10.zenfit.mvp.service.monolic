package x10.zenfit.mvp.service.service.voucherService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplyVoucherResponse {
    private String voucherId;
    private String code;
    private BigDecimal discountAmount;
    private BigDecimal subtotal;
    private BigDecimal totalAfterDiscount;
}
