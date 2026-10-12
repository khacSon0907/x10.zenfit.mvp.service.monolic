package x10.zenfit.mvp.service.infrastructure.refund.adapter;

import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.service.refundService.port.RefundPaymentCommand;
import x10.zenfit.mvp.service.service.refundService.port.RefundPaymentGateway;
import x10.zenfit.mvp.service.service.refundService.port.RefundPaymentResult;

@Component
public class PayOSRefundAdapter implements RefundPaymentGateway {

    @Override
    public String provider() {
        return "PAYOS";
    }

    @Override
    public RefundPaymentResult refund(RefundPaymentCommand command) {
        return RefundPaymentResult.builder()
                .success(false)
                .responseCode("PAYOS_REFUND_NOT_IMPLEMENTED")
                .responseMessage("payos-java 2.0.1 in this project does not expose a refund endpoint. Implement the official PayOS refund API call inside PayOSRefundAdapter.")
                .build();
    }
}
