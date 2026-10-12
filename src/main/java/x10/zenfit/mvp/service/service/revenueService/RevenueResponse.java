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
public class RevenueResponse {

    private String type;

    private LocalDate startDate;

    private LocalDate endDate;

    private BigDecimal totalRevenue;

    private long totalOrders;
}
