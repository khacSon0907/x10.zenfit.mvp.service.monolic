package x10.zenfit.mvp.service.service.notification.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.service.notification.EmailPort;
import x10.zenfit.mvp.service.service.notification.event.ForgotPasswordOtpEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class ForgotPasswordOtpListener {

    private final EmailPort emailPort;

    @Async
    @EventListener
    public void handleForgotPasswordOtpEvent(ForgotPasswordOtpEvent event) {
        log.info("Received ForgotPasswordOtpEvent for user {}", event.email());

        try {
            emailPort.sendForgotPasswordOtpEmail(
                    event.email(),
                    event.username(),
                    event.otp()
            );
            log.info("Successfully sent forgot password OTP email to {}", event.email());
        } catch (Exception e) {
            log.error("Failed to send forgot password OTP email to {}", event.email(), e);
        }
    }
}
