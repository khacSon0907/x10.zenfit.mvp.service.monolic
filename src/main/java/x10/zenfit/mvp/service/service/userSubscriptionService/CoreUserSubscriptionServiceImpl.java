package x10.zenfit.mvp.service.service.userSubscriptionService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.PaginatedResult;
import x10.zenfit.mvp.service.service.userSubscriptionService.cancelMySubscriptionUc.ICancelMySubscriptionUc;
import x10.zenfit.mvp.service.service.userSubscriptionService.getAdminSubscriptionsUc.IGetAdminSubscriptionsUc;
import x10.zenfit.mvp.service.service.userSubscriptionService.getAdminSubscriptionsUc.UserSubscriptionFilterQuery;
import x10.zenfit.mvp.service.service.userSubscriptionService.getMySubscriptionHistoryUc.IGetMySubscriptionHistoryUc;
import x10.zenfit.mvp.service.service.userSubscriptionService.getMySubscriptionUc.IGetMySubscriptionUc;
import x10.zenfit.mvp.service.service.userSubscriptionService.getSubscriptionByIdUc.IGetSubscriptionByIdUc;
import x10.zenfit.mvp.service.service.userSubscriptionService.selectPlanUc.ISelectPlanUc;
import x10.zenfit.mvp.service.service.userSubscriptionService.selectPlanUc.SelectPlanRequest;
import x10.zenfit.mvp.service.service.userSubscriptionService.updateSubscriptionStatusUc.IUpdateUserSubscriptionStatusUc;
import x10.zenfit.mvp.service.service.userSubscriptionService.updateSubscriptionStatusUc.UpdateUserSubscriptionStatusRequest;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoreUserSubscriptionServiceImpl implements ICoreUserSubscriptionService {

    private final ISelectPlanUc selectPlanUc;
    private final IGetMySubscriptionUc getMySubscriptionUc;
    private final IGetMySubscriptionHistoryUc getMySubscriptionHistoryUc;
    private final ICancelMySubscriptionUc cancelMySubscriptionUc;
    private final IGetAdminSubscriptionsUc getAdminSubscriptionsUc;
    private final IGetSubscriptionByIdUc getSubscriptionByIdUc;
    private final IUpdateUserSubscriptionStatusUc updateUserSubscriptionStatusUc;

    @Override
    public UserSubscriptionResponse selectPlan(String userId, SelectPlanRequest request) {
        return selectPlanUc.execute(userId, request);
    }

    @Override
    public UserSubscriptionResponse getMySubscription(String userId) {
        return getMySubscriptionUc.execute(userId);
    }

    @Override
    public List<UserSubscriptionResponse> getMySubscriptionHistory(String userId) {
        return getMySubscriptionHistoryUc.execute(userId);
    }

    @Override
    public UserSubscriptionResponse cancelMySubscription(String userId) {
        return cancelMySubscriptionUc.execute(userId);
    }

    @Override
    public PaginatedResult<UserSubscriptionResponse> getAdminSubscriptions(UserSubscriptionFilterQuery query) {
        return getAdminSubscriptionsUc.execute(query);
    }

    @Override
    public UserSubscriptionResponse getSubscriptionById(String id, String currentUserId, boolean isManagement) {
        return getSubscriptionByIdUc.execute(id, currentUserId, isManagement);
    }

    @Override
    public UserSubscriptionResponse updateSubscriptionStatus(String id, UpdateUserSubscriptionStatusRequest request) {
        return updateUserSubscriptionStatusUc.execute(id, request);
    }
}

