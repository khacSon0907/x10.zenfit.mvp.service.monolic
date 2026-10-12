package x10.zenfit.mvp.service.service.refundService.requestRefundUc;

import x10.zenfit.mvp.service.service.refundService.RefundRequest;
import x10.zenfit.mvp.service.service.refundService.RefundResponse;

public interface IRequestRefundUc {

    RefundResponse execute(String userId, RefundRequest request);
}
