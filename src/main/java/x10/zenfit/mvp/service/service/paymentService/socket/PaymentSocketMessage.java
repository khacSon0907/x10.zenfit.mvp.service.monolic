package x10.zenfit.mvp.service.service.paymentService.socket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.PaymentStatus;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSocketMessage {

    private String orderId;
    private Long orderCode;
    private PaymentStatus paymentStatus;
    private String message;

    private Boolean success;
    private String subscriptionId;
    private String planId;
    private String userId;
    private Long amount;
}
