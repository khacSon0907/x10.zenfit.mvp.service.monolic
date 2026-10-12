package x10.zenfit.mvp.service.service.refundService.approveRefundUc;

import x10.zenfit.mvp.service.service.refundService.RefundDecisionRequest;
import x10.zenfit.mvp.service.service.refundService.RefundResponse;

public interface IApproveRefundUc {

    RefundResponse execute(String refundId, RefundDecisionRequest request);
}
