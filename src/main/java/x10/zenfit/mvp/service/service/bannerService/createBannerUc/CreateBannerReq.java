package x10.zenfit.mvp.service.service.bannerService.createBannerUc;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateBannerReq {

    @Size(max = 200, message = "Tieu de banner khong duoc vuot qua 200 ky tu")
    private String title;

    private String subtitle;

    @NotBlank(message = "URL anh khong duoc de trong")
    private String imageUrl;

    private String linkUrl;

    private Boolean active;

    private Integer sortOrder;
}
