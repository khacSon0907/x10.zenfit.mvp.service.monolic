package x10.zenfit.mvp.service.service.voucherService;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import x10.zenfit.mvp.service.common.domain.enums.VoucherDiscountType;
import x10.zenfit.mvp.service.common.domain.enums.VoucherSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateVoucherRequest {
    @NotBlank(message = "Ma voucher khong duoc de trong")
    private String code;

    @NotBlank(message = "Ten voucher khong duoc de trong")
    private String name;

    private String description;

    @NotNull(message = "Loai giam gia khong duoc de trong")
    private VoucherDiscountType discountType;

    @NotNull(message = "Gia tri giam gia khong duoc de trong")
    @DecimalMin(value = "0.0", inclusive = false, message = "Gia tri giam gia phai lon hon 0")
    private BigDecimal discountValue;

    @DecimalMin(value = "0.0", message = "Gia tri don toi thieu khong duoc am")
    private BigDecimal minOrderAmount = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Muc giam toi da khong duoc am")
    private BigDecimal maxDiscountAmount;

    private Integer usageLimit = 1;
    private String assignedUserId;
    private VoucherSource source = VoucherSource.MANUAL;
    private String eventName;
    private Boolean active = true;

    @NotNull(message = "Thoi gian bat dau khong duoc de trong")
    private LocalDateTime startAt;

    @NotNull(message = "Thoi gian ket thuc khong duoc de trong")
    private LocalDateTime endAt;
}
