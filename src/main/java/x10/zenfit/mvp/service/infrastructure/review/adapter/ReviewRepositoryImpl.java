package x10.zenfit.mvp.service.infrastructure.review.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.Repository.IReviewRepository;
import x10.zenfit.mvp.service.common.domain.entities.review.ReviewEntity;
import x10.zenfit.mvp.service.infrastructure.review.db.mongodb.ReviewDocument;
import x10.zenfit.mvp.service.infrastructure.review.db.mongodb.ReviewMongoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements IReviewRepository {

    private final ReviewMongoRepository reviewMongoRepository;

    @Override
    public ReviewEntity save(ReviewEntity review) {
        ReviewDocument savedDocument = reviewMongoRepository.save(ReviewMapper.toDocument(review));
        return ReviewMapper.toEntity(savedDocument);
    }

    @Override
    public Optional<ReviewEntity> findById(String id) {
        return reviewMongoRepository.findById(id)
                .map(ReviewMapper::toEntity);
    }

    @Override
    public List<ReviewEntity> findAll() {
        return reviewMongoRepository.findAll().stream()
                .map(ReviewMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<ReviewEntity> findByProductId(String productId) {
        return reviewMongoRepository.findByProductIdOrderByCreatedAtDesc(productId).stream()
                .map(ReviewMapper::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(String id) {
        reviewMongoRepository.deleteById(id);
    }
}
