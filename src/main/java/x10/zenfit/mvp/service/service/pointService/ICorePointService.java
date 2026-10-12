package x10.zenfit.mvp.service.service.pointService;

import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;

import java.util.List;

public interface ICorePointService {
    PointResponse createRule(PointRequest request);
    PointResponse updateRule(String id, PointRequest request);
    List<PointResponse> getAllRules();
    PointResponse getActiveRule();
    void deleteRule(String id);
    long calculatePoints(OrderEntity order);
    long awardPointsForCompletedOrder(OrderEntity order);
}
