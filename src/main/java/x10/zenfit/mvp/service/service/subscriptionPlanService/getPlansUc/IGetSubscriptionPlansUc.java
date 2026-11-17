package x10.zenfit.mvp.service.service.subscriptionPlanService.getPlansUc;

import x10.zenfit.mvp.service.Repository.PaginatedResult;
import x10.zenfit.mvp.service.service.subscriptionPlanService.SubscriptionPlanResponse;

import java.util.List;

public interface IGetSubscriptionPlansUc {

    List<SubscriptionPlanResponse> getActivePlans();

    PaginatedResult<SubscriptionPlanResponse> getAdminPlans(SubscriptionPlanFilterQuery query);
}

