package x10.zenfit.mvp.service.share.response;


import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public final class ApiResponse<T> extends BaseResponse {

    private final T data;

    public static <T> ApiResponse<T> ok(T data) {
        return success(200, "SUCCESS", "OK", data);
    }

    public static <T> ApiResponse<T> ok(String code, String message, T data) {
        return success(200, code, message, data);
    }

    public static <T> ApiResponse<T> created(String code, String message, T data) {
        return success(201, code, message, data);
    }

    public static <T> ApiResponse<T> success(int status, String code, String message, T data) {
        return ApiResponse.<T>builder()
                .type("SUCCESS")
                .title("Success")
                .status(status)
                .code(code)
                .message(message)
                .data(data)
                .build();
    }
}