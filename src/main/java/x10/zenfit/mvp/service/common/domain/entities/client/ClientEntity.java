package x10.zenfit.mvp.service.common.domain.entities.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.ClientStatus;
import x10.zenfit.mvp.service.common.domain.enums.ExperienceLevel;
import x10.zenfit.mvp.service.common.domain.enums.FitnessGoal;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientEntity {
    // Thông tin cơ bản
    private String id;
    private String fullName;
    private String gender;
    private LocalDate dateOfBirth;
    private String phone;
    private String email;
    private String address;
    private String avatarUrl;
    private ClientStatus status;

    // Chỉ số cơ thể ban đầu
    private Double height;          // cm
    private Double initialWeight;   // kg

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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

