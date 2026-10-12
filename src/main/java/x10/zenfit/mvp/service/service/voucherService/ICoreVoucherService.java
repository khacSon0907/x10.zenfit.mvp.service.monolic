package x10.zenfit.mvp.service.service.voucherService;

import java.math.BigDecimal;
import java.util.List;

public interface ICoreVoucherService {
    VoucherResponse createVoucher(CreateVoucherRequest request);
    List<VoucherResponse> issueEventVouchers(IssueEventVouchersRequest request);
    VoucherResponse issueBirthdayVoucher(String userId, IssueBirthdayVoucherRequest request);
    List<VoucherResponse> getAll();
    VoucherResponse getVoucher(String id);
    List<VoucherResponse> getAvailableVouchers(String userId, BigDecimal subtotal);
    List<VoucherResponse> getMyVouchers(String userId);
    VoucherResponse updateVoucher(String id, UpdateVoucherRequest request);
    ApplyVoucherResponse applyVoucher(ApplyVoucherRequest request);
    VoucherCalculationResult validateAndCalculate(String code, String userId, BigDecimal subtotal);
    void markVoucherUsed(String voucherId);
    void deleteVoucher(String id);
}
