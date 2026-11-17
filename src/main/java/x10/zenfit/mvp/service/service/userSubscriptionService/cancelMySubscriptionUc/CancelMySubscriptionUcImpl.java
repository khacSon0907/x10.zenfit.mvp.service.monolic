package x10.zenfit.mvp.service.service.userSubscriptionService.cancelMySubscriptionUc;

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
import x10.zenfit.mvp.service.share.exception.user.UserError;

import java.time.Instant;

@Component
@RequiredArgsConstructor
public class CancelMySubscriptionUcImpl implements ICancelMySubscriptionUc {

    private final IUserSubscriptionRepository userSubscriptionRepository;
    private final ISubscriptionPlanRepository subscriptionPlanRepository;
    private final IUserRepository userRepository;

    @Override
    @Transactional
    public UserSubscriptionResponse execute(String userId) {
        if (userId == null || userId.isBlank()) {
            throw new BusinessException(UserError.USER_NOT_FOUND);
        }

        UserSubscriptionEntity subscription = userSubscriptionRepository.findActiveSubscriptionByUserId(userId.trim())
                .orElseThrow(() -> new BusinessException(UserSubscriptionError.NO_ACTIVE_SUBSCRIPTION));

        subscription.setStatus(UserSubscriptionStatus.CANCELLED);
        subscription.setUpdatedAt(Instant.now());
        UserSubscriptionEntity saved = userSubscriptionRepository.save(subscription);

        // Cập nhật user nếu không còn gói active nào
        boolean hasOtherActive = userSubscriptionRepository.existsByUserIdAndStatus(userId.trim(), UserSubscriptionStatus.ACTIVE);
        if (!hasOtherActive) {
            userRepository.findById(userId.trim()).ifPresent(u -> {
                u.setSubscriptionConfirmed(false);
                u.setUpdatedAt(Instant.now());
                userRepository.save(u);
            });
        }

        SubscriptionPlanEntity plan = subscriptionPlanRepository.findById(saved.getPlanId()).orElse(null);
        return UserSubscriptionResponse.fromEntity(saved, plan);
    }
}

