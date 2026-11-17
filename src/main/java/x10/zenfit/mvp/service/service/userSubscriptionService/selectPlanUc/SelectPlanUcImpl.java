package x10.zenfit.mvp.service.service.userSubscriptionService.selectPlanUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import x10.zenfit.mvp.service.Repository.ISubscriptionPlanRepository;
import x10.zenfit.mvp.service.Repository.IUserRepository;
import x10.zenfit.mvp.service.Repository.IUserSubscriptionRepository;
import x10.zenfit.mvp.service.common.domain.entities.subscription.SubscriptionPlanEntity;
import x10.zenfit.mvp.service.common.domain.entities.subscription.UserSubscriptionEntity;
import x10.zenfit.mvp.service.common.domain.entities.user.UserEntity;
import x10.zenfit.mvp.service.common.domain.enums.SubscriptionPlanStatus;
import x10.zenfit.mvp.service.common.domain.enums.UserSubscriptionStatus;
import x10.zenfit.mvp.service.service.userSubscriptionService.UserSubscriptionResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.subscription.UserSubscriptionError;
import x10.zenfit.mvp.service.share.exception.user.UserError;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SelectPlanUcImpl implements ISelectPlanUc {

    private final IUserSubscriptionRepository userSubscriptionRepository;
    private final ISubscriptionPlanRepository subscriptionPlanRepository;
    private final IUserRepository userRepository;

    @Override
    @Transactional
    public UserSubscriptionResponse execute(String userId, SelectPlanRequest request) {
        if (userId == null || userId.isBlank()) {
            throw new BusinessException(UserError.USER_NOT_FOUND);
        }

        if (request == null || request.getPlanId() == null || request.getPlanId().isBlank()) {
            throw new BusinessException(UserSubscriptionError.INVALID_SUBSCRIPTION_DATA);
        }

        UserEntity user = userRepository.findById(userId.trim())
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));

        SubscriptionPlanEntity plan = subscriptionPlanRepository.findById(request.getPlanId().trim())
                .orElseThrow(() -> new BusinessException(UserSubscriptionError.PLAN_NOT_FOUND));

        if (plan.getStatus() != SubscriptionPlanStatus.ACTIVE) {
            throw new BusinessException(UserSubscriptionError.PLAN_NOT_ACTIVE);
        }

        Instant now = Instant.now();

        // Kiểm tra xem user có gói đang ACTIVE không
        Optional<UserSubscriptionEntity> activeSubOpt = userSubscriptionRepository.findActiveSubscriptionByUserId(user.getId());
        if (activeSubOpt.isPresent()) {
            UserSubscriptionEntity activeSub = activeSubOpt.get();
            // Nếu cùng gói và chưa hết hạn thì báo lỗi trùng
            if (activeSub.getPlanId().equals(plan.getId()) && activeSub.getEndAt() != null && activeSub.getEndAt().isAfter(now)) {
                throw new BusinessException(UserSubscriptionError.ALREADY_HAS_ACTIVE_SUBSCRIPTION);
            }
            // Nếu đổi sang gói mới, chuyển gói cũ sang EXPIRED hoặc CANCELLED
            activeSub.setStatus(UserSubscriptionStatus.EXPIRED);
            activeSub.setUpdatedAt(now);
            userSubscriptionRepository.save(activeSub);
        }

        int durationDays = (plan.getDurationDays() != null && plan.getDurationDays() > 0)
                ? plan.getDurationDays()
                : 30;

        Instant endAt = now.plus(Duration.ofDays(durationDays));
        BigDecimal price = plan.getPrice() != null ? plan.getPrice() : BigDecimal.ZERO;
        boolean isFree = price.compareTo(BigDecimal.ZERO) == 0 || Boolean.TRUE.equals(plan.getIsDefault());

        UserSubscriptionEntity subscription = UserSubscriptionEntity.builder()
                .userId(user.getId())
                .planId(plan.getId())
                .status(UserSubscriptionStatus.ACTIVE)
                .startAt(now)
                .endAt(endAt)
                .price(price)
                .maxStudents(plan.getMaxStudents())
                .currentStudents(0)
                .autoRenew(Boolean.TRUE.equals(request.getAutoRenew()))
                .paidAt(now)
                .transactionId(isFree ? "FREE_TIER" : "DIRECT_REGISTRATION")
                .createdAt(now)
                .updatedAt(now)
                .build();

        UserSubscriptionEntity saved = userSubscriptionRepository.save(subscription);

        // Đánh dấu user đã chọn gói thành công
        user.setSubscriptionConfirmed(true);
        user.setUpdatedAt(now);
        userRepository.save(user);

        return UserSubscriptionResponse.fromEntity(saved, plan);
    }
}

