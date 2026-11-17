package x10.zenfit.mvp.service.service.subscriptionPlanService.updatePlanUc;

import x10.zenfit.mvp.service.service.subscriptionPlanService.SubscriptionPlanResponse;

public interface IUpdateSubscriptionPlanUc {

    SubscriptionPlanResponse execute(String id, UpdateSubscriptionPlanRequest request);
}

