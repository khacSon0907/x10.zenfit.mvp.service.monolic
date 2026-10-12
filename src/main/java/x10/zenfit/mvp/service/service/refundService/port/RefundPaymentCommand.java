package x10.zenfit.mvp.service.service.refundService.port;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class RefundPaymentCommand {

    private String paymentId;
    private String orderId;
    private String orderCode;
    private Long providerOrderCode;
    private BigDecimal amount;
    private String reason;
}
