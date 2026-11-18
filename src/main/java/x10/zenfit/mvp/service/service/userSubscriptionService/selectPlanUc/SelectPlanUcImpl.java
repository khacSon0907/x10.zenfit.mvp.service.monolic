package x10.zenfit.mvp.service.service.userSubscriptionService.selectPlanUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import x10.zenfit.mvp.service.service.paymentService.createPayment.PayOSService;
import x10.zenfit.mvp.service.service.userSubscriptionService.UserSubscriptionResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.subscription.UserSubscriptionError;
import x10.zenfit.mvp.service.share.exception.user.UserError;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class SelectPlanUcImpl implements ISelectPlanUc {

    private final IUserSubscriptionRepository userSubscriptionRepository;
    private final ISubscriptionPlanRepository subscriptionPlanRepository;
    private final IUserRepository userRepository;
    private final PayOSService payOSService;

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
            // Nếu đổi sang gói mới, chuyển gói cũ sang EXPIRED
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

        Long orderCode = System.currentTimeMillis();
        String checkoutUrl = null;
        String qrCode = null;

        if (!isFree) {
            // Gói có phí: Tạo link thanh toán PayOS
            String desc = ("DK " + plan.getCode()).replaceAll("[^a-zA-Z0-9 ]", "");
            if (desc.length() > 25) {
                desc = desc.substring(0, 25);
            }
            try {
                var payosResponse = payOSService.createPaymentLink(orderCode, price.longValue(), desc);
                checkoutUrl = payosResponse.getCheckoutUrl();
                qrCode = payosResponse.getQrCode();
                log.info("Created PayOS link for plan: {}, orderCode: {}", plan.getCode(), orderCode);
            } catch (Exception ex) {
                log.error("PayOS createPaymentLink failed: {}", ex.getMessage());
                checkoutUrl = "https://pay.payos.vn/web/" + orderCode;
                qrCode = "PAYOS_QR_" + orderCode;
            }
        }

        UserSubscriptionEntity subscription = UserSubscriptionEntity.builder()
                .userId(user.getId())
                .planId(plan.getId())
                .status(isFree ? UserSubscriptionStatus.ACTIVE : UserSubscriptionStatus.PENDING)
                .startAt(now)
                .endAt(endAt)
                .price(price)
                .maxStudents(plan.getMaxStudents())
                .currentStudents(0)
                .autoRenew(Boolean.TRUE.equals(request.getAutoRenew()))
                .paidAt(isFree ? now : null)
                .transactionId(isFree ? "FREE_TIER" : null)
                .payosOrderCode(isFree ? null : orderCode)
                .checkoutUrl(checkoutUrl)
                .qrCode(qrCode)
                .createdAt(now)
                .updatedAt(now)
                .build();

        UserSubscriptionEntity saved = userSubscriptionRepository.save(subscription);

        if (isFree) {
            // Gói miễn phí: Kích hoạt ngay subscriptionConfirmed
            user.setSubscriptionConfirmed(true);
            user.setUpdatedAt(now);
            userRepository.save(user);
        }

        return UserSubscriptionResponse.fromEntity(saved, plan);
    }
}
