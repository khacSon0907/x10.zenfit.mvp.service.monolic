package x10.zenfit.mvp.service.service.subscriptionPlanService.getPlanUc;

import x10.zenfit.mvp.service.service.subscriptionPlanService.SubscriptionPlanResponse;

public interface IGetSubscriptionPlanUc {

    SubscriptionPlanResponse getById(String id);

    SubscriptionPlanResponse getDefaultPlan();
}

