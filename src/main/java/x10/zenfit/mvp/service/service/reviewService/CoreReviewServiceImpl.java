package x10.zenfit.mvp.service.service.reviewService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import x10.zenfit.mvp.service.Repository.IProductRepository;
import x10.zenfit.mvp.service.Repository.IReviewRepository;
import x10.zenfit.mvp.service.Repository.IUserRepository;
import x10.zenfit.mvp.service.common.domain.entities.review.ReviewEntity;
import x10.zenfit.mvp.service.common.domain.entities.user.UserEntity;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.product.ProductError;
import x10.zenfit.mvp.service.share.exception.review.ReviewError;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CoreReviewServiceImpl implements ICoreReviewService {

    private final IReviewRepository reviewRepository;
    private final IProductRepository productRepository;
    private final IUserRepository userRepository;

    @Override
    public ReviewResponse createReview(ReviewRequest request) {
        validateProductExists(request.getProductId());
        validateRating(request.getRating());

        LocalDateTime now = LocalDateTime.now();
        ReviewEntity review = ReviewEntity.builder()
                .productId(request.getProductId())
                .userId(request.getUserId())
                .rating(request.getRating())
                .comment(request.getComment())
                .images(request.getImages())
                .createdAt(now)
                .updatedAt(now)
                .build();

        return toResponse(reviewRepository.save(review));
    }

    @Override
    public ReviewResponse getReviewById(String id) {
        return toResponse(findReviewOrThrow(id));
    }

    @Override
    public List<ReviewResponse> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public List<ReviewResponse> getReviewsByProductId(String productId) {
        validateProductExists(productId);
        return reviewRepository.findByProductId(productId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ReviewSummaryResponse getReviewSummaryByProductId(String productId) {
        validateProductExists(productId);
        List<ReviewEntity> reviews = reviewRepository.findByProductId(productId);

        Map<Integer, Long> ratingCounts = new LinkedHashMap<>();
        for (int rating = 0; rating <= 5; rating++) {
            final int currentRating = rating;
            long count = reviews.stream()
                    .filter(review -> currentRating == defaultRating(review.getRating()))
                    .count();
            ratingCounts.put(currentRating, count);
        }

        double averageRating = reviews.isEmpty()
                ? 0
                : reviews.stream()
                .mapToInt(review -> defaultRating(review.getRating()))
                .average()
                .orElse(0);

        return ReviewSummaryResponse.builder()
                .productId(productId)
                .totalReviews(reviews.size())
                .averageRating(Math.round(averageRating * 10.0) / 10.0)
                .ratingCounts(ratingCounts)
                .build();
    }

    @Override
    public ReviewResponse updateReview(String id, ReviewRequest request) {
        ReviewEntity review = findReviewOrThrow(id);
        validateProductExists(request.getProductId());
        validateRating(request.getRating());

        review.setProductId(request.getProductId());
        review.setUserId(request.getUserId());
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setImages(request.getImages());
        review.setUpdatedAt(LocalDateTime.now());

        return toResponse(reviewRepository.save(review));
    }

    @Override
    public void deleteReview(String id) {
        findReviewOrThrow(id);
        reviewRepository.deleteById(id);
    }

    private ReviewEntity findReviewOrThrow(String id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ReviewError.REVIEW_NOT_FOUND));
    }

    private void validateProductExists(String productId) {
        productRepository.findById(productId)
                .orElseThrow(() -> new BusinessException(ProductError.PRODUCT_NOT_FOUND));
    }

    private void validateRating(Integer rating) {
        if (rating == null || rating < 0 || rating > 5) {
            throw new BusinessException(ReviewError.INVALID_RATING);
        }
    }

    private int defaultRating(Integer rating) {
        return rating == null ? 0 : rating;
    }

    private ReviewResponse toResponse(ReviewEntity review) {
        ReviewResponse response = ReviewResponseMapper.toResponse(review);
        if (response == null || !StringUtils.hasText(review.getUserId())) {
            return response;
        }

        userRepository.findById(review.getUserId()).ifPresent(user -> {
            response.setUserName(displayName(user));
            response.setUserAvatarUrl(user.getAvatarUrl());
        });

        return response;
    }

    private String displayName(UserEntity user) {
        if (StringUtils.hasText(user.getUsername())) {
            return user.getUsername();
        }

        if (StringUtils.hasText(user.getEmail())) {
            return user.getEmail();
        }

        return user.getId();
    }
}
