package x10.zenfit.mvp.service.service.userService.updateUserUc;

import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {

    @Size(
            min = 3,
            max = 30,
            message = "Tên người dùng phải từ 3 đến 30 ký tự"
    )
    @Pattern(
            regexp = "^[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)*$",
            message = "Tên người dùng chỉ được chứa chữ, số, dấu gạch dưới và dấu chấm (không bắt đầu/kết thúc bằng dấu chấm, không có hai dấu chấm liên tiếp)"
    )
    private String username;

    @Pattern(
            regexp = "^(0|\\+84)(3[2-9]|5[2689]|7[06-9]|8[1-9]|9\\d)\\d{7}$",
            message = "Số điện thoại không hợp lệ"
    )
    private String phoneNumber;

    @Size(
            max = 500,
            message = "URL ảnh đại diện tối đa 500 ký tự"
    )
    private String avatarUrl;

    @PastOrPresent(
            message = "Ngày sinh không được lớn hơn ngày hiện tại"
    )
    private LocalDate birthday;
}