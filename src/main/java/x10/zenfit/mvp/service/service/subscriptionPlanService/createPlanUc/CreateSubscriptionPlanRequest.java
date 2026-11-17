package x10.zenfit.mvp.service.service.subscriptionPlanService.createPlanUc;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.SubscriptionPlanStatus;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSubscriptionPlanRequest {

    @NotBlank(message = "Mã gói không được để trống")
    @Pattern(regexp = "^[A-Za-z0-9_-]{2,30}$", message = "Mã gói chỉ gồm 2-30 ký tự chữ cái, số, gạch ngang hoặc gạch dưới")
    private String code;

    @NotBlank(message = "Tên gói không được để trống")
    @Size(max = 100, message = "Tên gói tối đa 100 ký tự")
    private String name;

    @Size(max = 500, message = "Mô tả tối đa 500 ký tự")
    private String description;

    @NotNull(message = "Giá gói không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Giá gói không được âm")
    private BigDecimal price;

    @NotNull(message = "Chu kỳ sử dụng không được để trống")
    @Min(value = 1, message = "Chu kỳ sử dụng tối thiểu 1 ngày")
    private Integer durationDays;

    @NotNull(message = "Giới hạn học viên không được để trống")
    @Min(value = 0, message = "Giới hạn học viên không được âm")
    private Integer maxStudents;

    @Min(value = 0, message = "Giới hạn buổi tập không được âm")
    private Integer maxTrainingSessions;

    private SubscriptionPlanStatus status;

    private Boolean isDefault;

    private Integer displayOrder;
}

