package x10.zenfit.mvp.service.service.userSubscriptionService.getMySubscriptionHistoryUc;

import x10.zenfit.mvp.service.service.userSubscriptionService.UserSubscriptionResponse;

import java.util.List;

public interface IGetMySubscriptionHistoryUc {

    List<UserSubscriptionResponse> execute(String userId);
}

