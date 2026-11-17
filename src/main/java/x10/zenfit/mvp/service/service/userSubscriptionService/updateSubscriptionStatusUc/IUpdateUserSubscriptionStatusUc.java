package x10.zenfit.mvp.service.service.userSubscriptionService.updateSubscriptionStatusUc;

import x10.zenfit.mvp.service.service.userSubscriptionService.UserSubscriptionResponse;

public interface IUpdateUserSubscriptionStatusUc {

    UserSubscriptionResponse execute(String id, UpdateUserSubscriptionStatusRequest request);
}

