package x10.zenfit.mvp.service.service.notification.event;

public record ForgotPasswordOtpEvent(
        String email,
        String username,
        String otp
) {
}
