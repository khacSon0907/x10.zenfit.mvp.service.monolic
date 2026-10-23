package x10.zenfit.mvp.service.service.clientService.createClientUc;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.ClientStatus;
import x10.zenfit.mvp.service.common.domain.enums.ExperienceLevel;
import x10.zenfit.mvp.service.common.domain.enums.FitnessGoal;

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

    // Chỉ số cơ thể ban đầu
    private Double height;

    private Double initialWeight;

    // Mục tiêu & trình độ
    private FitnessGoal fitnessGoal;

    private ExperienceLevel experienceLevel;

    private Double targetWeight;

    // Sức khỏe
    private String medicalConditions;

    private String injuries;

    // Liên hệ khẩn cấp
    private String emergencyContactName;

    private String emergencyContactPhone;

    // PT phụ trách
    private String assignedTrainerId;

    // Quản lý
    private String notes;

    private LocalDate joinedDate;
}
