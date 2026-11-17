package x10.zenfit.mvp.service.service.userSubscriptionService.getAdminSubscriptionsUc;

import x10.zenfit.mvp.service.Repository.PaginatedResult;
import x10.zenfit.mvp.service.service.userSubscriptionService.UserSubscriptionResponse;

public interface IGetAdminSubscriptionsUc {

    PaginatedResult<UserSubscriptionResponse> execute(UserSubscriptionFilterQuery query);
}

