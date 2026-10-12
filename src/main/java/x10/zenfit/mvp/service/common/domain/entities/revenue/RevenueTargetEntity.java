package x10.zenfit.mvp.service.common.domain.entities.revenue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenueTargetEntity {
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
