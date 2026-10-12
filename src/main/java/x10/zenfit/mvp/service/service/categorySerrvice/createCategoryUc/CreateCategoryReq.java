package x10.zenfit.mvp.service.service.categorySerrvice.createCategoryUc;

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
public class CreateCategoryReq {

    @NotBlank(message = "Ten danh muc khong duoc de trong")
    @Size(min = 2, max = 100, message = "Ten danh muc phai tu 2 den 100 ky tu")
    private String name;

    private String description;

    private String bannerUrl;

    private Boolean active;

    private String parentId;

    private Integer sortOrder;
}
