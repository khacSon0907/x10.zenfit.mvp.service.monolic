package x10.zenfit.mvp.service.service.revenueService;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RevenueTargetRequest {

    @NotNull(message = "Nam target khong duoc de trong")
    @Min(value = 2000, message = "Nam target khong hop le")
    private Integer year;

    @NotNull(message = "Thang target khong duoc de trong")
    @Min(value = 1, message = "Thang target phai tu 1 den 12")
    @Max(value = 12, message = "Thang target phai tu 1 den 12")
    private Integer month;

    @NotNull(message = "Target doanh thu khong duoc de trong")
    @DecimalMin(value = "0.0", inclusive = false, message = "Target doanh thu phai lon hon 0")
    private BigDecimal targetRevenue;

    @NotNull(message = "Target so don khong duoc de trong")
    @Min(value = 1, message = "Target so don phai lon hon 0")
    private Long targetOrders;

    private String note;
}
