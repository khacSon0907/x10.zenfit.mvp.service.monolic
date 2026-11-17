package x10.zenfit.mvp.service.service.userSubscriptionService.updateSubscriptionStatusUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import x10.zenfit.mvp.service.Repository.ISubscriptionPlanRepository;
import x10.zenfit.mvp.service.Repository.IUserRepository;
import x10.zenfit.mvp.service.Repository.IUserSubscriptionRepository;
import x10.zenfit.mvp.service.common.domain.entities.subscription.SubscriptionPlanEntity;
import x10.zenfit.mvp.service.common.domain.entities.subscription.UserSubscriptionEntity;
import x10.zenfit.mvp.service.common.domain.enums.UserSubscriptionStatus;
import x10.zenfit.mvp.service.service.userSubscriptionService.UserSubscriptionResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.subscription.UserSubscriptionError;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class UpdateUserSubscriptionStatusUcImpl implements IUpdateUserSubscriptionStatusUc {

    private final IUserSubscriptionRepository userSubscriptionRepository;
    private final ISubscriptionPlanRepository subscriptionPlanRepository;
    private final IUserRepository userRepository;

    @Override
    @Transactional
    public UserSubscriptionResponse execute(String id, UpdateUserSubscriptionStatusRequest request) {
        if (id == null || id.isBlank()) {
            throw new BusinessException(UserSubscriptionError.SUBSCRIPTION_NOT_FOUND);
        }

        if (request == null || request.getStatus() == null) {
            throw new BusinessException(UserSubscriptionError.INVALID_SUBSCRIPTION_DATA);
        }

        UserSubscriptionEntity subscription = userSubscriptionRepository.findById(id.trim())
                .orElseThrow(() -> new BusinessException(UserSubscriptionError.SUBSCRIPTION_NOT_FOUND));

        subscription.setStatus(request.getStatus());
        subscription.setUpdatedAt(Instant.now());
        UserSubscriptionEntity saved = userSubscriptionRepository.save(subscription);

        // Đồng bộ trạng thái user nếu chuyển sang ACTIVE hoặc không còn ACTIVE
        boolean hasActive = userSubscriptionRepository.existsByUserIdAndStatus(saved.getUserId(), UserSubscriptionStatus.ACTIVE);
        userRepository.findById(saved.getUserId()).ifPresent(user -> {
            user.setSubscriptionConfirmed(hasActive);
            user.setUpdatedAt(Instant.now());
            userRepository.save(user);
        });

        SubscriptionPlanEntity plan = subscriptionPlanRepository.findById(saved.getPlanId()).orElse(null);
        return UserSubscriptionResponse.fromEntity(saved, plan);
    }
}

