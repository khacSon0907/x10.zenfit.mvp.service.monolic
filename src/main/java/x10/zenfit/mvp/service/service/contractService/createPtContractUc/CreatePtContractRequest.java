package x10.zenfit.mvp.service.service.contractService.createPtContractUc;

import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.ContractStatus;
import x10.zenfit.mvp.service.service.clientService.createClientUc.CreateClientRequest;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePtContractRequest {

    // 1. Mã khách hàng nếu đã tồn tại và được chọn từ hệ thống (kết quả tìm kiếm)
    private String clientId;

    // 2. Tên tìm kiếm hoặc tra cứu khách hàng có sẵn
    private String searchClientName;

    // 3. Thông tin khách hàng dạng object lồng (nếu nhập thông tin khách hàng mới)
    @Valid
    private CreateClientRequest clientInfo;

    // 4. Các trường thông tin khách hàng dạng phẳng
    private String clientFullName;
    private String clientPhone;
    private String clientEmail;
    private String clientGender;
    private LocalDate clientDateOfBirth;
    private String clientAddress;
    private String clientAvatarUrl;

    // Ảnh hình thể ban đầu
    private String clientFrontPhotoUrl;
    private String clientBackPhotoUrl;
    private String clientSidePhotoUrl;

    // Chỉ số cơ thể ban đầu
    private Double height;          // cm
    private Double initialWeight;   // kg
    private String clientNotes;

    // 5. Thông tin mục tiêu tập luyện (ClientGoalEntity)
    private String goalType;        // BULKING, CUTTING, GIẢM CÂN, TĂNG CƠ, v.v.
    private Double targetWeight;    // kg
    private Double startWeight;     // kg (nếu để trống sẽ lấy initialWeight)
    private String goalNote;

    // 6. Thông tin hợp đồng PT (PtContractEntity)
    @NotBlank(message = "Mã huấn luyện viên (trainerId) không được để trống")
    private String trainerId;

    @NotNull(message = "Tổng số buổi tập không được để trống")
    @Min(value = 1, message = "Tổng số buổi tập phải từ 1 buổi trở lên")
    private Integer totalSessions;

    private Integer bonusSessions;

    private LocalDate startDate;
    private LocalDate endDate;

    @NotNull(message = "Giá trị hợp đồng không được để trống")
    @DecimalMin(value = "0.0", inclusive = true, message = "Giá trị hợp đồng không được âm")
    private BigDecimal price;

    private BigDecimal paidAmount;

    private ContractStatus status;
    private String notes;
}
