package x10.zenfit.mvp.service.service.clientService.createClientUc;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.ClientStatus;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateClientRequest {

    @NotBlank(message = "Họ và tên không được để trống")
    private String fullName;

    private String gender;

    private LocalDate dateOfBirth;

    @NotBlank(message = "Số điện thoại không được để trống")
    private String phone;

    @Email(message = "Định dạng email không hợp lệ")
    private String email;

    private String address;

    private String avatarUrl;

    private ClientStatus status;

    private String frontPhotoUrl;
    private String backPhotoUrl;
    private String sidePhotoUrl;

    // Chỉ số cơ thể ban đầu
    private Double height;

    private Double initialWeight;

    // PT phụ trách
    private String assignedTrainerId;

    // Quản lý
    private String notes;

    private LocalDate joinedDate;
}
