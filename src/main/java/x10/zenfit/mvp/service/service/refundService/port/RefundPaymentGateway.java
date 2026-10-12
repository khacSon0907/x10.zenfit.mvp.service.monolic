package x10.zenfit.mvp.service.service.refundService.port;

public interface RefundPaymentGateway {

    String provider();

    RefundPaymentResult refund(RefundPaymentCommand command);
}
