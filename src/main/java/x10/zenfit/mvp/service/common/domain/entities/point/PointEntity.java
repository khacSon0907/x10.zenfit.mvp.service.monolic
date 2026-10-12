package x10.zenfit.mvp.service.common.domain.entities.point;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PointEntity {
    private String id;
    private String name;
    private BigDecimal moneyAmount;
    private Long points;
    private Boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
