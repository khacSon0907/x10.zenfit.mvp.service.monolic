package x10.zenfit.mvp.service.service.promotionBannerService.updatePromotionBannerUc;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePromotionBannerReq {
    private String id;

    @Size(max = 200, message = "Tiêu đề promotion banner không được vượt quá 200 ký tự")
    private String title;

    private Boolean active;
    private Integer sortOrder;
    private Instant startDate;
    private Instant endDate;
}
