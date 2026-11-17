package x10.zenfit.mvp.service.service.userSubscriptionService;

import x10.zenfit.mvp.service.Repository.PaginatedResult;
import x10.zenfit.mvp.service.service.userSubscriptionService.getAdminSubscriptionsUc.UserSubscriptionFilterQuery;
import x10.zenfit.mvp.service.service.userSubscriptionService.selectPlanUc.SelectPlanRequest;
import x10.zenfit.mvp.service.service.userSubscriptionService.updateSubscriptionStatusUc.UpdateUserSubscriptionStatusRequest;

import java.util.List;

public interface ICoreUserSubscriptionService {

    UserSubscriptionResponse selectPlan(String userId, SelectPlanRequest request);

    UserSubscriptionResponse getMySubscription(String userId);

    List<UserSubscriptionResponse> getMySubscriptionHistory(String userId);

    UserSubscriptionResponse cancelMySubscription(String userId);

    PaginatedResult<UserSubscriptionResponse> getAdminSubscriptions(UserSubscriptionFilterQuery query);

    UserSubscriptionResponse getSubscriptionById(String id, String currentUserId, boolean isManagement);

    UserSubscriptionResponse updateSubscriptionStatus(String id, UpdateUserSubscriptionStatusRequest request);
}

