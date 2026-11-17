package x10.zenfit.mvp.service.service.subscriptionPlanService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.PaginatedResult;
import x10.zenfit.mvp.service.service.subscriptionPlanService.createPlanUc.CreateSubscriptionPlanRequest;
import x10.zenfit.mvp.service.service.subscriptionPlanService.createPlanUc.ICreateSubscriptionPlanUc;
import x10.zenfit.mvp.service.service.subscriptionPlanService.deletePlanUc.IDeleteSubscriptionPlanUc;
import x10.zenfit.mvp.service.service.subscriptionPlanService.getPlanUc.IGetSubscriptionPlanUc;
import x10.zenfit.mvp.service.service.subscriptionPlanService.getPlansUc.IGetSubscriptionPlansUc;
import x10.zenfit.mvp.service.service.subscriptionPlanService.getPlansUc.SubscriptionPlanFilterQuery;
import x10.zenfit.mvp.service.service.subscriptionPlanService.updatePlanStatusUc.IUpdateSubscriptionPlanStatusUc;
import x10.zenfit.mvp.service.service.subscriptionPlanService.updatePlanStatusUc.UpdateSubscriptionPlanStatusRequest;
import x10.zenfit.mvp.service.service.subscriptionPlanService.updatePlanUc.IUpdateSubscriptionPlanUc;
import x10.zenfit.mvp.service.service.subscriptionPlanService.updatePlanUc.UpdateSubscriptionPlanRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoreSubscriptionPlanServiceImpl implements ICoreSubscriptionPlanService {

    private final ICreateSubscriptionPlanUc createSubscriptionPlanUc;
    private final IGetSubscriptionPlansUc getSubscriptionPlansUc;
    private final IGetSubscriptionPlanUc getSubscriptionPlanUc;
    private final IUpdateSubscriptionPlanUc updateSubscriptionPlanUc;
    private final IUpdateSubscriptionPlanStatusUc updateSubscriptionPlanStatusUc;
    private final IDeleteSubscriptionPlanUc deleteSubscriptionPlanUc;

    @Override
    public SubscriptionPlanResponse createPlan(CreateSubscriptionPlanRequest request) {
        return createSubscriptionPlanUc.execute(request);
    }

    @Override
    public List<SubscriptionPlanResponse> getActivePlans() {
        return getSubscriptionPlansUc.getActivePlans();
    }

    @Override
    public PaginatedResult<SubscriptionPlanResponse> getAdminPlans(SubscriptionPlanFilterQuery query) {
        return getSubscriptionPlansUc.getAdminPlans(query);
    }

    @Override
    public SubscriptionPlanResponse getPlanById(String id) {
        return getSubscriptionPlanUc.getById(id);
    }

    @Override
    public SubscriptionPlanResponse getDefaultPlan() {
        return getSubscriptionPlanUc.getDefaultPlan();
    }

    @Override
    public SubscriptionPlanResponse updatePlan(String id, UpdateSubscriptionPlanRequest request) {
        return updateSubscriptionPlanUc.execute(id, request);
    }

    @Override
    public SubscriptionPlanResponse updatePlanStatus(String id, UpdateSubscriptionPlanStatusRequest request) {
        return updateSubscriptionPlanStatusUc.execute(id, request);
    }

    @Override
    public void deletePlan(String id) {
        deleteSubscriptionPlanUc.execute(id);
    }
}

