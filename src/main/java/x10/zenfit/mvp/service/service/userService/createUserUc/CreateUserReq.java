package x10.zenfit.mvp.service.service.userService.createUserUc;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
public class CreateUserReq {

    @NotBlank(message = "Tên người dùng không được để trống")
    @Size(min = 3, max = 30, message = "Tên người dùng phải từ 3 đến 30 ký tự")
    @Pattern(regexp = "^[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*$",
            message = "Tên người dùng chỉ được chứa chữ, số, dấu gạch dưới và dấu chấm (không bắt đầu/kết thúc bằng dấu chấm, không có hai dấu chấm liên tiếp)")
    private String username;

    @NotBlank(message = "Email không được để trống")
    @Email(regexp = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$", message = "Email không hợp lệ")
    @Size(max = 100, message = "Email tối đa 100 ký tự")
    private String email;

    // Nếu SĐT không bắt buộc: bỏ @NotBlank (client nên gửi null thay vì chuỗi rỗng)
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0|\\+84)(3[2-9]|5[2689]|7[06-9]|8[1-9]|9\\d)\\d{7}$",
            message = "Số điện thoại không hợp lệ")
    private String phone;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, max = 50, message = "Mật khẩu phải từ 8 đến 50 ký tự")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
            message = "Mật khẩu phải chứa cả chữ và số")
    @ToString.Exclude
    private String password;

    /**
     * Danh sách role ID được gán (tuỳ chọn).
     * Mặc định khi tạo mới nếu không truyền hoặc rỗng: Role PT.
     */
    private List<String> roleIds;
}