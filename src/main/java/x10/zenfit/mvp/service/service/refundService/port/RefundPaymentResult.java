package x10.zenfit.mvp.service.service.refundService.port;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RefundPaymentResult {

    private boolean success;
    private String transactionId;
    private String responseCode;
    private String responseMessage;
}
