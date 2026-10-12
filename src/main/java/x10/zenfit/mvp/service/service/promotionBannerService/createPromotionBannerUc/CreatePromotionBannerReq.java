package x10.zenfit.mvp.service.service.promotionBannerService.createPromotionBannerUc;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class CreatePromotionBannerReq {

    @NotBlank(message = "Tiêu đề promotion banner không được để trống")
    @Size(max = 200, message = "Tiêu đề promotion banner không được vượt quá 200 ký tự")
    private String title;

    private Boolean active;
    private Integer sortOrder;

    @NotNull(message = "Ngày bắt đầu promotion banner không được để trống")
    private Instant startDate;

    @NotNull(message = "Ngày kết thúc promotion banner không được để trống")
    private Instant endDate;
}
