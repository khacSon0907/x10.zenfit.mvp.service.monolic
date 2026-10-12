package x10.zenfit.mvp.service.service.voucherService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.entities.voucher.VoucherEntity;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherCalculationResult {
    private VoucherEntity voucher;
    private BigDecimal discountAmount;
}
