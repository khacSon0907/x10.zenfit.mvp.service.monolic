package x10.zenfit.mvp.service.service.paymentService.createPayment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePaymentLinkRequest {

    private String orderId;
    private String subscriptionId;
}
