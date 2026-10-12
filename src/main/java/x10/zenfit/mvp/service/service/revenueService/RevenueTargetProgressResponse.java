package x10.zenfit.mvp.service.service.revenueService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenueTargetProgressResponse {
    private RevenueTargetResponse target;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal currentRevenue;
    private Long currentOrders;
    private BigDecimal remainingRevenue;
    private Long remainingOrders;
    private BigDecimal revenueProgressPercent;
    private BigDecimal orderProgressPercent;
    private Boolean revenueTargetAchieved;
    private Boolean orderTargetAchieved;
}
