package x10.zenfit.mvp.service.infrastructure.promotionBanner.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "promotion_banners")
public class PromotionBannerDocument {
    @Id
    private String id;
    private String title;
    private boolean active;
    private Integer sortOrder;
    private Instant startDate;
    private Instant endDate;
}
