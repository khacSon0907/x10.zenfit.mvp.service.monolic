package x10.zenfit.mvp.service.service.bannerService.updateBannerUc;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBannerReq {

    private String id; // set từ path variable

    @Size(max = 200, message = "Tiêu đề banner không được vượt quá 200 ký tự")
    private String title;

    private String subtitle;
    private String imageUrl;
    private String linkUrl;
    private Boolean active;
    private Integer sortOrder;
}
