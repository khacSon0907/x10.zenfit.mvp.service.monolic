package x10.zenfit.mvp.service.service.refundService;

import x10.zenfit.mvp.service.service.refundService.getRefundUc.RefundCursorPageResponse;

import java.util.List;

public interface ICoreRefundService {

    RefundResponse requestRefund(String userId, RefundRequest request);

    List<RefundResponse> getMyRefunds(String userId);

    List<RefundResponse> getAllRefunds();

    RefundCursorPageResponse getAllRefundsByCursor(String cursor, Integer limit);

    RefundResponse approveRefund(String refundId, RefundDecisionRequest request);

    RefundResponse markReturnReceived(String refundId, RefundDecisionRequest request);

    RefundResponse markRefunded(String refundId, RefundDecisionRequest request);

    RefundResponse rejectRefund(String refundId, RefundDecisionRequest request);
}
