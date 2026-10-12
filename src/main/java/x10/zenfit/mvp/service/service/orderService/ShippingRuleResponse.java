package x10.zenfit.mvp.service.service.orderService;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ShippingRuleResponse {
    private String id;
    private String name;
    private BigDecimal defaultShippingFee;
    private BigDecimal freeShippingThreshold;
    private List<LocalDate> freeShippingDates;
    private Boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
