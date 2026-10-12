package x10.zenfit.mvp.service.service.notification.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.service.notification.EmailPort;
import x10.zenfit.mvp.service.service.notification.event.RegisterOtpEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class RegisterOtpEventListener {

    private final EmailPort emailPort;

    @Async("mailExecutor")
    @EventListener
    public void handleRegisterOtpEvent(RegisterOtpEvent event) {

        try {
            emailPort.sendRegisterOtpEmail(
                    event.email(),
                    event.username(),
                    event.otp()
            );
        } catch (Exception e) {
            log.error(
                    "Failed to send register OTP email to: {}",
                    event.email(),
                    e
            );
        }
    }
}