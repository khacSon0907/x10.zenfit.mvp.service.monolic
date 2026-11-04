package x10.zenfit.mvp.service.service.contractService.createPtContractUc;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.PaymentContractStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePtContractRequest {

    // Mã học viên
    @NotBlank(message = "Mã học viên không được để trống")
    private String clientId;

    // Mã huấn luyện viên
    @NotBlank(message = "Mã huấn luyện viên không được để trống")
    private String trainerId;

    // Tổng số buổi mua
    @NotNull(message = "Tổng số buổi tập không được để trống")
    @Min(value = 1, message = "Tổng số buổi tập phải từ 1 buổi trở lên")
    private Integer totalSessions;

    // Số buổi tặng
    @Min(value = 0, message = "Số buổi tặng không được âm")
    @Builder.Default
    private Integer bonusSessions = 0;

    // Ngày bắt đầu
    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDate startDate;

    // Ngày hết hạn
    @NotNull(message = "Ngày hết hạn không được để trống")
    private LocalDate endDate;

    // Giá trị hợp đồng
    @NotNull(message = "Giá trị hợp đồng không được để trống")
    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Giá trị hợp đồng phải lớn hơn 0"
    )
    private BigDecimal price;

    // Giá tiền một buổi tập (nếu không truyền sẽ tự động tính từ price / totalSessions)
    @DecimalMin(
            value = "0.0",
            inclusive = false,
            message = "Giá mỗi buổi tập phải lớn hơn 0"
    )
    private BigDecimal pricePerSession;

    // Số tiền đã thanh toán
    @DecimalMin(
            value = "0.0",
            inclusive = true,
            message = "Số tiền thanh toán không được âm"
    )
    @Builder.Default
    private BigDecimal paidAmount = BigDecimal.ZERO;

    // Trạng thái thanh toán (nếu không truyền sẽ tự động xác định dựa vào paidAmount và price)
    private PaymentContractStatus paymentContractStatus;

    // Ghi chú
    @Size(max = 1000, message = "Ghi chú tối đa 1000 ký tự")
    private String notes;
}