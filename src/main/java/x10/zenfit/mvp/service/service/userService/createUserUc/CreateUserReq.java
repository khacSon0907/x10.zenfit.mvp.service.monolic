package x10.zenfit.mvp.service.service.userService.createUserUc;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateUserReq {

    @NotBlank(message = "Tên người dùng không được để trống")
    @Size(min = 3, max = 30, message = "Tên người dùng phải từ 3 đến 30 ký tự")
    private String username;

    @Email(message = "Email không hợp lệ")
    @NotBlank(message = "Email không được để trống")
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 6, max = 50, message = "Mật khẩu phải từ 6 đến 50 ký tự")
    private String password;
}
