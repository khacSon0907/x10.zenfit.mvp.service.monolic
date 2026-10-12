package x10.zenfit.mvp.service.service.orderService;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class ShippingRuleRequest {
    @NotBlank(message = "Ten cau hinh phi ship khong duoc de trong")
    private String name;

    @DecimalMin(value = "0.0", message = "Phi ship mac dinh khong duoc am")
    private BigDecimal defaultShippingFee = BigDecimal.ZERO;

    @DecimalMin(value = "0.0", message = "Nguong free ship khong duoc am")
    private BigDecimal freeShippingThreshold;

    private List<LocalDate> freeShippingDates = new ArrayList<>();

    private Boolean active = true;
}
