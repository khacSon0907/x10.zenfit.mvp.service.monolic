package x10.zenfit.mvp.service.infrastructure.client.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import x10.zenfit.mvp.service.common.domain.enums.ClientStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "clients")
public class ClientDocument {

    @Id
    private String id;

    // Thông tin cơ bản
    private String fullName;
    private String gender;
    private LocalDate dateOfBirth;
    private String phone;
    private String email;
    private String address;
    private String avatarUrl;
    private ClientStatus status;

    // Ảnh hình thể
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
