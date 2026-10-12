package x10.zenfit.mvp.service.service.pointService;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PointRequest {
    @NotBlank(message = "Ten rule diem khong duoc de trong")
    private String name;

    @NotNull(message = "So tien quy doi diem khong duoc de trong")
    @DecimalMin(value = "0.0", inclusive = false, message = "So tien quy doi diem phai lon hon 0")
    private BigDecimal moneyAmount;

    @NotNull(message = "So diem nhan duoc khong duoc de trong")
    @Min(value = 1, message = "So diem nhan duoc phai lon hon 0")
    private Long points;

    private Boolean active = true;
}
