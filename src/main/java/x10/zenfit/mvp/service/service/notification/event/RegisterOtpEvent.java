package x10.zenfit.mvp.service.service.notification.event;

public record RegisterOtpEvent(
        String email,
        String username,
        String otp
) {
}