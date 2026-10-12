package x10.zenfit.mvp.service.service.pointService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IPointRepository;
import x10.zenfit.mvp.service.Repository.IUserRepository;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;
import x10.zenfit.mvp.service.common.domain.entities.point.PointEntity;
import x10.zenfit.mvp.service.common.domain.entities.user.UserEntity;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.CommonError;
import x10.zenfit.mvp.service.share.exception.user.UserError;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CorePointServiceImpl implements ICorePointService {

    private final IPointRepository pointRepository;
    private final IUserRepository userRepository;

    @Override
    public PointResponse createRule(PointRequest request) {
        Instant now = Instant.now();
        boolean active = request.getActive() == null || request.getActive();

        if (active) {
            deactivateActiveRules(null, now);
        }

        PointEntity entity = PointEntity.builder()
                .id(UUID.randomUUID().toString())
                .name(request.getName().trim())
                .moneyAmount(request.getMoneyAmount())
                .points(request.getPoints())
                .active(active)
                .createdAt(now)
                .updatedAt(now)
                .build();

        return PointResponseMapper.toResponse(pointRepository.save(entity));
    }

    @Override
    public PointResponse updateRule(String id, PointRequest request) {
        PointEntity existing = pointRepository.findById(id)
                .orElseThrow(() -> new BusinessException(CommonError.NOT_FOUND, "Khong tim thay rule diem"));

        Instant now = Instant.now();
        boolean active = request.getActive() == null || request.getActive();

        if (active) {
            deactivateActiveRules(id, now);
        }

        existing.setName(request.getName().trim());
        existing.setMoneyAmount(request.getMoneyAmount());
        existing.setPoints(request.getPoints());
        existing.setActive(active);
        existing.setUpdatedAt(now);

        return PointResponseMapper.toResponse(pointRepository.save(existing));
    }

    @Override
    public List<PointResponse> getAllRules() {
        return pointRepository.findAll()
                .stream()
                .map(PointResponseMapper::toResponse)
                .toList();
    }

    @Override
    public PointResponse getActiveRule() {
        return pointRepository.findActiveRule()
                .map(PointResponseMapper::toResponse)
                .orElse(null);
    }

    @Override
    public void deleteRule(String id) {
        PointEntity existing = pointRepository.findById(id)
                .orElseThrow(() -> new BusinessException(CommonError.NOT_FOUND, "Khong tim thay rule diem"));

        pointRepository.deleteById(existing.getId());
    }

    @Override
    public long calculatePoints(OrderEntity order) {
        if (order == null || order.getTotalAmount() == null || order.getTotalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            return 0L;
        }

        return pointRepository.findActiveRule()
                .map(rule -> calculateFromRule(rule, order.getTotalAmount()))
                .orElse(0L);
    }

    @Override
    public long awardPointsForCompletedOrder(OrderEntity order) {
        if (order == null
                || order.getUserId() == null
                || order.getUserId().isBlank()
                || Boolean.TRUE.equals(order.getPointsAwarded())) {
            return defaultPoints(order);
        }

        long earnedPoints = calculatePoints(order);
        order.setPointsEarned(earnedPoints);
        order.setPointsAwarded(true);

        if (earnedPoints <= 0) {
            return earnedPoints;
        }

        UserEntity user = userRepository.findById(order.getUserId())
                .orElseThrow(() -> new BusinessException(UserError.USER_NOT_FOUND));

        user.setPoints(defaultPoints(user) + earnedPoints);
        user.setUpdatedAt(Instant.now());
        userRepository.save(user);

        return earnedPoints;
    }

    private long calculateFromRule(PointEntity rule, BigDecimal totalAmount) {
        if (rule.getMoneyAmount() == null
                || rule.getMoneyAmount().compareTo(BigDecimal.ZERO) <= 0
                || rule.getPoints() == null
                || rule.getPoints() <= 0) {
            return 0L;
        }

        long units = totalAmount
                .divide(rule.getMoneyAmount(), 0, RoundingMode.DOWN)
                .longValue();

        return units * rule.getPoints();
    }

    private void deactivateActiveRules(String keepId, Instant now) {
        List<PointEntity> activeRules = pointRepository.findAllActive()
                .stream()
                .filter(rule -> keepId == null || !keepId.equals(rule.getId()))
                .peek(rule -> {
                    rule.setActive(false);
                    rule.setUpdatedAt(now);
                })
                .toList();

        if (!activeRules.isEmpty()) {
            pointRepository.saveAll(activeRules);
        }
    }

    private long defaultPoints(UserEntity user) {
        return user == null || user.getPoints() == null ? 0L : user.getPoints();
    }

    private long defaultPoints(OrderEntity order) {
        return order == null || order.getPointsEarned() == null ? 0L : order.getPointsEarned();
    }
}
