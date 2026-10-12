package x10.zenfit.mvp.service.service.bannerService.createBannerUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBannerResp {
    private String id;
    private String title;
    private String subtitle;
    private String imageUrl;
    private String linkUrl;
    private Boolean active;
    private Integer sortOrder;
    private Instant createdAt;
    private Instant updatedAt;
}
