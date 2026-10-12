package x10.zenfit.mvp.service.service.voucherService;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import x10.zenfit.mvp.service.common.domain.enums.VoucherDiscountType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class IssueEventVouchersRequest {
    @NotBlank(message = "Ten su kien khong duoc de trong")
    private String eventName;

    @NotBlank(message = "Tien to ma voucher khong duoc de trong")
    private String codePrefix;

    @NotEmpty(message = "Danh sach user khong duoc de trong")
    private List<String> userIds;

    @NotNull(message = "Loai giam gia khong duoc de trong")
    private VoucherDiscountType discountType;

    @NotNull(message = "Gia tri giam gia khong duoc de trong")
    @DecimalMin(value = "0.0", inclusive = false, message = "Gia tri giam gia phai lon hon 0")
    private BigDecimal discountValue;

    @DecimalMin(value = "0.0", message = "Gia tri don toi thieu khong duoc am")
    private BigDecimal minOrderAmount = BigDecimal.ZERO;

    private BigDecimal maxDiscountAmount;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
