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

    // 1. Mã khách hàng nếu đã tồn tại và được chọn từ hệ thống
    private String clientId;

    // 2. Thông tin khách hàng dạng object lồng (nếu nhập thông tin khách hàng mới / tìm kiếm)
    @Valid
    private CreateClientRequest clientInfo;

    // 3. Các trường thông tin khách hàng phẳng tiện lợi (nếu frontend gửi phẳng thay vì object clientInfo)
    private String clientFullName;
    private String clientPhone;
    private String clientEmail;
    private String clientGender;
    private String clientAddress;

    // 4. Thông tin hợp đồng
    @NotBlank(message = "Mã huấn luyện viên (trainerId) không được để trống")
    private String trainerId;

    private String goal;
    private Double targetWeight;

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
