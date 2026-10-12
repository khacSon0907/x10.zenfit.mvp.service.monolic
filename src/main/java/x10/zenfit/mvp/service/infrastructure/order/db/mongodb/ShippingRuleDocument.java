package x10.zenfit.mvp.service.infrastructure.order.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "shipping_rules")
public class ShippingRuleDocument {
    @Id
    private String id;
    private String name;
    private BigDecimal defaultShippingFee;
    private BigDecimal freeShippingThreshold;
    private List<LocalDate> freeShippingDates;
    private Boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
