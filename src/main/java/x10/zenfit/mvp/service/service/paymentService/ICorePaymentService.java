package x10.zenfit.mvp.service.service.paymentService;

import x10.zenfit.mvp.service.service.paymentService.createPayment.CreatePaymentLinkRequest;
import x10.zenfit.mvp.service.service.paymentService.createPayment.CreatePaymentLinkResponse;

public interface ICorePaymentService {
    CreatePaymentLinkResponse createPaymentLink(CreatePaymentLinkRequest request);
}
