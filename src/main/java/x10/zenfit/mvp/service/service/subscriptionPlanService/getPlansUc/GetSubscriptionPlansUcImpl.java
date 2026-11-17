package x10.zenfit.mvp.service.service.subscriptionPlanService.getPlansUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.ISubscriptionPlanRepository;
import x10.zenfit.mvp.service.Repository.PaginatedResult;
import x10.zenfit.mvp.service.common.domain.entities.subscription.SubscriptionPlanEntity;
import x10.zenfit.mvp.service.service.subscriptionPlanService.SubscriptionPlanResponse;

import java.util.List;

@Component
@RequiredArgsConstructor
public class GetSubscriptionPlansUcImpl implements IGetSubscriptionPlansUc {

    private final ISubscriptionPlanRepository planRepository;

    @Override
    public List<SubscriptionPlanResponse> getActivePlans() {
        return planRepository.findActivePlans()
                .stream()
                .map(SubscriptionPlanResponse::fromEntity)
                .toList();
    }

    @Override
    public PaginatedResult<SubscriptionPlanResponse> getAdminPlans(SubscriptionPlanFilterQuery query) {
        String keyword = query != null ? query.getKeyword() : null;
        var status = query != null ? query.getStatus() : null;
        int page = query != null && query.getPage() != null ? query.getPage() : 0;
        int size = query != null && query.getSize() != null ? query.getSize() : 10;

        PaginatedResult<SubscriptionPlanEntity> entityResult = planRepository.findAdminPlans(
                keyword,
                status,
                page,
                size
        );

        List<SubscriptionPlanResponse> dtoList = entityResult.getItems()
                .stream()
                .map(SubscriptionPlanResponse::fromEntity)
                .toList();

        return PaginatedResult.<SubscriptionPlanResponse>builder()
                .items(dtoList)
                .page(entityResult.getPage())
                .size(entityResult.getSize())
                .totalElements(entityResult.getTotalElements())
                .totalPages(entityResult.getTotalPages())
                .hasNext(entityResult.isHasNext())
                .hasPrevious(entityResult.isHasPrevious())
                .build();
    }
}

