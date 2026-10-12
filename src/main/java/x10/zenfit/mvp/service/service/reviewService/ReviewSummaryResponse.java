package x10.zenfit.mvp.service.service.reviewService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewSummaryResponse {

    private String productId;

    private long totalReviews;

    private double averageRating;

    private Map<Integer, Long> ratingCounts;
}
