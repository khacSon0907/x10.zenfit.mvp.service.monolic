package x10.zenfit.mvp.service.service.revenueService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopSellingProductResponse {

    private String productId;

    private String productName;

    private String productImage;

    private long totalQuantitySold;

    private BigDecimal totalRevenue;
}
