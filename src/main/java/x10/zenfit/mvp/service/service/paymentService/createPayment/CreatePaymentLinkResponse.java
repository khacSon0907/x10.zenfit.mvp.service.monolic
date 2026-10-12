package x10.zenfit.mvp.service.service.paymentService.createPayment;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class CreatePaymentLinkResponse {

    private Long orderCode;

    private String paymentLinkId;

    private String checkoutUrl;

    private String qrCode;

    private BigDecimal amount;

    private String orderId;
}