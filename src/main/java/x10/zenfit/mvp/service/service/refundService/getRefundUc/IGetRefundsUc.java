package x10.zenfit.mvp.service.service.refundService.getRefundUc;

import x10.zenfit.mvp.service.service.refundService.RefundResponse;

import java.util.List;

public interface IGetRefundsUc {

    List<RefundResponse> getMyRefunds(String userId);

    List<RefundResponse> getAllRefunds();

    RefundCursorPageResponse getAllRefundsByCursor(String cursor, Integer limit);
}
