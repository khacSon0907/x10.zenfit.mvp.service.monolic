package x10.zenfit.mvp.service.service.notification;

import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;

public interface EmailPort {

    void sendRegisterOtpEmail(
            String to,
            String username,
            String otp
    );

    void sendForgotPasswordOtpEmail(
            String to,
            String username,
            String otp
    );

    void sendOrderInvoiceEmail(
            String to,
            String username,
            OrderEntity order
    );
}
