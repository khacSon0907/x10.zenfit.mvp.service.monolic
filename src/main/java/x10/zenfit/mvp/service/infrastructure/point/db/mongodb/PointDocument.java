package x10.zenfit.mvp.service.infrastructure.point.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "point_rules")
public class PointDocument {
    @Id
    private String id;
    private String name;
    private BigDecimal moneyAmount;
    private Long points;
    private Boolean active;
    private Instant createdAt;
    private Instant updatedAt;
}
