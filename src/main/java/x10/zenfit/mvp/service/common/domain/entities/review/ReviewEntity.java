package x10.zenfit.mvp.service.common.domain.entities.review;

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
public class ReviewEntity {

    private String id;

    private String productId;

    private String userId;

    private Integer rating;

    private String comment;

    private List<String> images;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
