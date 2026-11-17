package x10.zenfit.mvp.service.service.subscriptionPlanService.updatePlanStatusUc;

import x10.zenfit.mvp.service.service.subscriptionPlanService.SubscriptionPlanResponse;

public interface IUpdateSubscriptionPlanStatusUc {

    SubscriptionPlanResponse execute(String id, UpdateSubscriptionPlanStatusRequest request);
}

