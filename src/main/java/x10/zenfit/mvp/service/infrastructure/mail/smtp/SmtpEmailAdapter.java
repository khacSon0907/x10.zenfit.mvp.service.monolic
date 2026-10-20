package x10.zenfit.mvp.service.infrastructure.mail.smtp;

import lombok.RequiredArgsConstructor;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;
import x10.zenfit.mvp.service.infrastructure.mail.template.ForgotPasswordEmailTemplate;
import x10.zenfit.mvp.service.infrastructure.mail.template.OrderInvoiceEmailTemplate;
import x10.zenfit.mvp.service.infrastructure.mail.template.RegisterOtpEmailTemplate;
import x10.zenfit.mvp.service.service.notification.EmailPort;

@Component
@RequiredArgsConstructor
public class SmtpEmailAdapter implements EmailPort {

    private final JavaMailSender javaMailSender;

    @Value("${app.mail.from}")
    private String mailFrom;

    @Override
    public void sendRegisterOtpEmail(
            String to,
            String username,
            String otp
    ) {
        sendHtmlEmail(
                to,
                "[Zenfit] Mã xác thực đăng ký tài khoản",
                RegisterOtpEmailTemplate.build(username, otp)
        );
    }

    @Override
    public void sendForgotPasswordOtpEmail(
            String to,
            String username,
            String otp
    ) {
        sendHtmlEmail(
                to,
                "[Zenfit] Mã xác thực đặt lại mật khẩu",
                ForgotPasswordEmailTemplate.build(username, otp)
        );
    }

    @Override
    public void sendOrderInvoiceEmail(
            String to,
            String username,
            OrderEntity order
    ) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

            helper.setFrom(mailFrom);
            helper.setTo(to);
            helper.setSubject("Zenfit invoice - " + order.getOrderCode());
            helper.setText(
                    OrderInvoiceEmailTemplate.build(username, order),
                    true
            );

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new MailPreparationException("Failed to prepare order invoice email", e);
        }
    }

    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

            helper.setFrom(mailFrom);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            throw new MailPreparationException("Failed to prepare email", e);
        }
    }
}
