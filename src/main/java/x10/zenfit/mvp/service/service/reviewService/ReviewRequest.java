package x10.zenfit.mvp.service.service.reviewService;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {

    @NotBlank(message = "Product id is required")
    private String productId;

    @NotBlank(message = "User id is required")
    private String userId;

    @NotNull(message = "Rating is required")
    @Min(value = 0, message = "Rating must be from 0 to 5")
    @Max(value = 5, message = "Rating must be from 0 to 5")
    private Integer rating;

    private String comment;

    private List<String> images;
}
