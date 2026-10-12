package x10.zenfit.mvp.service.service.authService.resetPasswordUc;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordReq {

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    private String email;

    @NotBlank(message = "Reset token không được để trống")
    private String resetToken;

    @NotBlank(message = "Mật khẩu mới không được để trống")
    private String newPassword;
}
