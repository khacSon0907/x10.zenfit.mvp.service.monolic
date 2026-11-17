package x10.zenfit.mvp.service.infrastructure.subscription.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.ISubscriptionPlanRepository;
import x10.zenfit.mvp.service.Repository.PaginatedResult;
import x10.zenfit.mvp.service.common.domain.entities.subscription.SubscriptionPlanEntity;
import x10.zenfit.mvp.service.common.domain.enums.SubscriptionPlanStatus;
import x10.zenfit.mvp.service.infrastructure.subscription.adapter.mapper.SubscriptionPlanMapper;
import x10.zenfit.mvp.service.infrastructure.subscription.db.mongodb.SubscriptionPlanDocument;
import x10.zenfit.mvp.service.infrastructure.subscription.db.mongodb.SubscriptionPlanMongoRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SubscriptionPlanRepositoryImpl implements ISubscriptionPlanRepository {

    private final SubscriptionPlanMongoRepository mongoRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public SubscriptionPlanEntity save(SubscriptionPlanEntity plan) {
        if (plan == null) {
            return null;
        }

        SubscriptionPlanDocument document = SubscriptionPlanMapper.toDocument(plan);
        SubscriptionPlanDocument saved = mongoRepository.save(document);
        return SubscriptionPlanMapper.toEntity(saved);
    }

    @Override
    public Optional<SubscriptionPlanEntity> findById(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }

        return mongoRepository.findById(id.trim()).map(SubscriptionPlanMapper::toEntity);
    }

    @Override
    public Optional<SubscriptionPlanEntity> findByCode(String code) {
        if (code == null || code.isBlank()) {
            return Optional.empty();
        }

        return mongoRepository.findByCodeIgnoreCase(code.trim()).map(SubscriptionPlanMapper::toEntity);
    }

    @Override
    public boolean existsByCode(String code) {
        if (code == null || code.isBlank()) {
            return false;
        }

        return mongoRepository.existsByCodeIgnoreCase(code.trim());
    }

    @Override
    public boolean existsByCodeAndIdNot(String code, String id) {
        if (code == null || code.isBlank() || id == null || id.isBlank()) {
            return false;
        }

        return mongoRepository.existsByCodeIgnoreCaseAndIdNot(code.trim(), id.trim());
    }

    @Override
    public Optional<SubscriptionPlanEntity> findDefaultPlan() {
        return mongoRepository.findFirstByIsDefaultTrue().map(SubscriptionPlanMapper::toEntity);
    }

    @Override
    public void clearDefaultFlagExcept(String planId) {
        Query query = new Query(Criteria.where("isDefault").is(true));
        if (planId != null && !planId.isBlank()) {
            query.addCriteria(Criteria.where("_id").ne(planId.trim()));
        }

        Update update = new Update()
                .set("isDefault", false)
                .set("updatedAt", LocalDateTime.now());

        mongoTemplate.updateMulti(query, update, SubscriptionPlanDocument.class);
    }

    @Override
    public List<SubscriptionPlanEntity> findActivePlans() {
        return mongoRepository.findByStatusOrderByDisplayOrderAsc(SubscriptionPlanStatus.ACTIVE)
                .stream()
                .map(SubscriptionPlanMapper::toEntity)
                .toList();
    }

    @Override
    public PaginatedResult<SubscriptionPlanEntity> findAdminPlans(
            String keyword,
            SubscriptionPlanStatus status,
            int page,
            int size
    ) {
        List<Criteria> criteriaList = new ArrayList<>();

        if (keyword != null && !keyword.isBlank()) {
            String kw = keyword.trim();
            Criteria keywordCriteria = new Criteria().orOperator(
                    Criteria.where("code").regex(kw, "i"),
                    Criteria.where("name").regex(kw, "i")
            );
            criteriaList.add(keywordCriteria);
        }

        if (status != null) {
            criteriaList.add(Criteria.where("status").is(status));
        }

        Query query = new Query();
        if (!criteriaList.isEmpty()) {
            query.addCriteria(new Criteria().andOperator(criteriaList.toArray(new Criteria[0])));
        }

        long totalElements = mongoTemplate.count(query, SubscriptionPlanDocument.class);

        int effectivePage = Math.max(0, page);
        int effectiveSize = (size <= 0) ? 10 : size;

        query.with(Sort.by(Sort.Direction.ASC, "displayOrder").and(Sort.by(Sort.Direction.DESC, "createdAt")));
        query.skip((long) effectivePage * effectiveSize).limit(effectiveSize);

        List<SubscriptionPlanEntity> items = mongoTemplate.find(query, SubscriptionPlanDocument.class)
                .stream()
                .map(SubscriptionPlanMapper::toEntity)
                .toList();

        int totalPages = (effectiveSize == 0) ? 0 : (int) Math.ceil((double) totalElements / effectiveSize);

        return PaginatedResult.<SubscriptionPlanEntity>builder()
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
    public void deleteById(String id) {
        if (id != null && !id.isBlank()) {
            mongoRepository.deleteById(id.trim());
        }
    }
}

