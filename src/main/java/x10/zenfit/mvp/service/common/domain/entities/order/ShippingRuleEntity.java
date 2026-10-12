package x10.zenfit.mvp.service.common.domain.entities.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShippingRuleEntity {
    private String id;
    private String name;
    private BigDecimal defaultShippingFee;
    private BigDecimal freeShippingThreshold;
    private List<LocalDate> freeShippingDates;
    private Boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
