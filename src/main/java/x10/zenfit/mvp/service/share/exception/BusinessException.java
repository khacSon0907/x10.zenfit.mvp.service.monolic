package x10.zenfit.mvp.service.share.exception;



import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorDescriptor error;
    private final Object detail;
    private final Long retryAfterSeconds;  // 🆕 For rate limit

    public BusinessException(ErrorDescriptor error) {
        super(error.defaultMessage());
        this.error = error;
        this.detail = null;
        this.retryAfterSeconds = null;
    }

    public BusinessException(ErrorDescriptor error, String customMessage) {
        super(customMessage);
        this.error = error;
        this.detail = null;
        this.retryAfterSeconds = null;
    }

    public BusinessException(ErrorDescriptor error, Object detail) {
        super(error.defaultMessage());
        this.error = error;
        this.detail = detail;
        this.retryAfterSeconds = null;
    }

    public BusinessException(ErrorDescriptor error, String customMessage, Object detail) {
        super(customMessage);
        this.error = error;
        this.detail = detail;
        this.retryAfterSeconds = null;
    }

    // 🆕 Constructor for rate limit with retryAfterSeconds
    public BusinessException(ErrorDescriptor error, long retryAfterSeconds) {
        super(error.defaultMessage());
        this.error = error;
        this.detail = null;
        this.retryAfterSeconds = retryAfterSeconds;
    }

    // 🆕 Constructor for rate limit with custom message
    public BusinessException(ErrorDescriptor error, String customMessage, long retryAfterSeconds) {
        super(customMessage);
        this.error = error;
        this.detail = null;
        this.retryAfterSeconds = retryAfterSeconds;
    }

    public String messageToClient() {
        return getMessage();
    }
}