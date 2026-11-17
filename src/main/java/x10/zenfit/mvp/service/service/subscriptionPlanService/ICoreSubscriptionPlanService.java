package x10.zenfit.mvp.service.service.subscriptionPlanService;

import x10.zenfit.mvp.service.Repository.PaginatedResult;
import x10.zenfit.mvp.service.service.subscriptionPlanService.createPlanUc.CreateSubscriptionPlanRequest;
import x10.zenfit.mvp.service.service.subscriptionPlanService.getPlansUc.SubscriptionPlanFilterQuery;
import x10.zenfit.mvp.service.service.subscriptionPlanService.updatePlanStatusUc.UpdateSubscriptionPlanStatusRequest;
import x10.zenfit.mvp.service.service.subscriptionPlanService.updatePlanUc.UpdateSubscriptionPlanRequest;

import java.util.List;

public interface ICoreSubscriptionPlanService {

    SubscriptionPlanResponse createPlan(CreateSubscriptionPlanRequest request);

    List<SubscriptionPlanResponse> getActivePlans();

    PaginatedResult<SubscriptionPlanResponse> getAdminPlans(SubscriptionPlanFilterQuery query);

    SubscriptionPlanResponse getPlanById(String id);

    SubscriptionPlanResponse getDefaultPlan();

    SubscriptionPlanResponse updatePlan(String id, UpdateSubscriptionPlanRequest request);

    SubscriptionPlanResponse updatePlanStatus(String id, UpdateSubscriptionPlanStatusRequest request);

    void deletePlan(String id);
}

