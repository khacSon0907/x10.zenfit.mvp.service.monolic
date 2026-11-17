package x10.zenfit.mvp.service.service.userSubscriptionService.selectPlanUc;

import x10.zenfit.mvp.service.service.userSubscriptionService.UserSubscriptionResponse;

public interface ISelectPlanUc {

    UserSubscriptionResponse execute(String userId, SelectPlanRequest request);
}

