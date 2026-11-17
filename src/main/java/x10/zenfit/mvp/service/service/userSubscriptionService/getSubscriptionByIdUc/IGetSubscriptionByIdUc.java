package x10.zenfit.mvp.service.service.userSubscriptionService.getSubscriptionByIdUc;

import x10.zenfit.mvp.service.service.userSubscriptionService.UserSubscriptionResponse;

public interface IGetSubscriptionByIdUc {

    UserSubscriptionResponse execute(String id, String currentUserId, boolean isManagement);
}

