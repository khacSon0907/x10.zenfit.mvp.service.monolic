package x10.zenfit.mvp.service.common.domain.entities.banner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromotionBannerEntity {

    private String id;

    private String title;

    private boolean active;

    private Integer sortOrder;

    private Instant startDate;

    private Instant endDate;
}