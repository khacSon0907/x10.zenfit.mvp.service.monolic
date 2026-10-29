package x10.zenfit.mvp.service.common.domain.entities.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.ClientStatus;

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

    private String frontPhotoUrl;   // Mặt trước
    private String backPhotoUrl;    // Mặt sau
    private String sidePhotoUrl;    // Mặt nghiêng

    // Chỉ số cơ thể ban đầu
    private Double height;          // cm
    private Double initialWeight;   // kg

    // PT phụ trách
    private String assignedTrainerId;

    // Quản lý
    private String notes;
    private LocalDate joinedDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

