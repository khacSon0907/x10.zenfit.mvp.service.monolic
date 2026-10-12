package x10.zenfit.mvp.service.service.paymentService.createPayment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.exception.PayOSException;
import x10.zenfit.mvp.service.config.payos.PayOSProperties;

@Service
@RequiredArgsConstructor
public class PayOSService {

    private final PayOS payOS;
    private final PayOSProperties properties;

    public vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse createPaymentLink(
            Long orderCode,
            Long amount,
            String description
    ) {
        try {
            vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest paymentData =
                    vn.payos.model.v2.paymentRequests.CreatePaymentLinkRequest.builder()
                            .orderCode(orderCode)
                            .amount(amount)
                            .description(description)
                            .returnUrl(properties.getReturnUrl())
                            .cancelUrl(properties.getCancelUrl())
                            .build();

            return payOS.paymentRequests().create(paymentData);

        } catch (PayOSException e) {
            throw new RuntimeException("Create payOS payment link failed: " + e.getMessage(), e);
        }
    }
}