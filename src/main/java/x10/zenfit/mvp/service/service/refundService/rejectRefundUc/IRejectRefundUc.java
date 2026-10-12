package x10.zenfit.mvp.service.service.refundService.rejectRefundUc;

import x10.zenfit.mvp.service.service.refundService.RefundDecisionRequest;
import x10.zenfit.mvp.service.service.refundService.RefundResponse;

public interface IRejectRefundUc {

    RefundResponse execute(String refundId, RefundDecisionRequest request);
}
