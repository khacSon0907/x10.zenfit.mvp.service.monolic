package x10.zenfit.mvp.service.infrastructure.revenue.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "revenue_targets")
@CompoundIndex(name = "year_month_unique", def = "{'year': 1, 'month': 1}", unique = true)
public class RevenueTargetDocument {
    @Id
    private String id;
    private Integer year;
    private Integer month;
    private BigDecimal targetRevenue;
    private Long targetOrders;
    private String note;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
