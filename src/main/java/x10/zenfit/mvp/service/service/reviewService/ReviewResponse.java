package x10.zenfit.mvp.service.service.reviewService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {

    private String id;

    private String productId;

    private String userId;

    private String userName;

    private String userAvatarUrl;

    private Integer rating;

    private String comment;

    private List<String> images;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
