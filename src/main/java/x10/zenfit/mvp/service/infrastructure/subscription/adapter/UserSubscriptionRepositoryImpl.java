package x10.zenfit.mvp.service.infrastructure.subscription.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IUserSubscriptionRepository;
import x10.zenfit.mvp.service.Repository.PaginatedResult;
import x10.zenfit.mvp.service.common.domain.entities.subscription.UserSubscriptionEntity;
import x10.zenfit.mvp.service.common.domain.enums.UserSubscriptionStatus;
import x10.zenfit.mvp.service.infrastructure.subscription.adapter.mapper.UserSubscriptionMapper;
import x10.zenfit.mvp.service.infrastructure.subscription.db.mongodb.UserSubscriptionDocument;
import x10.zenfit.mvp.service.infrastructure.subscription.db.mongodb.UserSubscriptionMongoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserSubscriptionRepositoryImpl implements IUserSubscriptionRepository {

    private final UserSubscriptionMongoRepository mongoRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public UserSubscriptionEntity save(UserSubscriptionEntity subscription) {
        if (subscription == null) {
            return null;
        }

        UserSubscriptionDocument document = UserSubscriptionMapper.toDocument(subscription);
        UserSubscriptionDocument saved = mongoRepository.save(document);
        return UserSubscriptionMapper.toEntity(saved);
    }

    @Override
    public Optional<UserSubscriptionEntity> findById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }

        return mongoRepository.findById(id.trim()).map(UserSubscriptionMapper::toEntity);
    }

    @Override
    public Optional<UserSubscriptionEntity> findActiveSubscriptionByUserId(String userId) {
        if (userId == null || userId.isBlank()) {
            return Optional.empty();
        }

        return mongoRepository.findFirstByUserIdAndStatusOrderByCreatedAtDesc(userId.trim(), UserSubscriptionStatus.ACTIVE)
                .map(UserSubscriptionMapper::toEntity);
    }

    @Override
    public List<UserSubscriptionEntity> findByUserIdOrderByCreatedAtDesc(String userId) {
        if (userId == null || userId.isBlank()) {
            return List.of();
        }

        return mongoRepository.findByUserIdOrderByCreatedAtDesc(userId.trim())
                .stream()
                .map(UserSubscriptionMapper::toEntity)
                .toList();
    }

    @Override
    public PaginatedResult<UserSubscriptionEntity> findSubscriptions(
            String userId,
            String planId,
            UserSubscriptionStatus status,
            int page,
            int size
    ) {
        List<Criteria> criteriaList = new ArrayList<>();

        if (userId != null && !userId.isBlank()) {
            criteriaList.add(Criteria.where("userId").is(userId.trim()));
        }

        if (planId != null && !planId.isBlank()) {
            criteriaList.add(Criteria.where("planId").is(planId.trim()));
        }

        if (status != null) {
            criteriaList.add(Criteria.where("status").is(status));
        }

        Query query = new Query();
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        long totalElements = mongoTemplate.count(query, UserSubscriptionDocument.class);

        int effectivePage = Math.max(0, page);
        int effectiveSize = (size <= 0) ? 10 : size;

        query.with(Sort.by(Sort.Direction.DESC, "createdAt"));
        query.skip((long) effectivePage * effectiveSize).limit(effectiveSize);

        List<UserSubscriptionEntity> items = mongoTemplate.find(query, UserSubscriptionDocument.class)
                .stream()
                .map(UserSubscriptionMapper::toEntity)
                .toList();

        int totalPages = (effectiveSize == 0) ? 0 : (int) Math.ceil((double) totalElements / effectiveSize);

        return PaginatedResult.<UserSubscriptionEntity>builder()
                .items(items)
                .page(effectivePage)
                .size(effectiveSize)
                .totalElements(totalElements)
                .totalPages(totalPages)
                .hasNext(effectivePage + 1 < totalPages)
                .hasPrevious(effectivePage > 0)
                .build();
    }

    @Override
    public boolean existsByUserIdAndStatus(String userId, UserSubscriptionStatus status) {
        if (userId == null || userId.isBlank() || status == null) {
            return false;
        }

        return mongoRepository.existsByUserIdAndStatus(userId.trim(), status);
    }

    @Override
    public void deleteById(String id) {
        if (id != null && !id.isBlank()) {
            mongoRepository.deleteById(id.trim());
        }
    }
}

