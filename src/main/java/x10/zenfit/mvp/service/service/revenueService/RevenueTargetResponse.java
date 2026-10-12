package x10.zenfit.mvp.service.service.revenueService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RevenueTargetResponse {
    private String id;
    private Integer year;
    private Integer month;
    private BigDecimal targetRevenue;
    private Long targetOrders;
    private String note;
    private String createdBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
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
