package x10.zenfit.mvp.service.service.pointService;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
public class PointResponse {
    private String id;
    private String name;
    private BigDecimal moneyAmount;
    private Long points;
    private Boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
