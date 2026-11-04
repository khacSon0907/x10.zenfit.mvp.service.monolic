package x10.zenfit.mvp.service.service.clientGoalService.createGoalUc;

import jakarta.validation.constraints.*;
import lombok.*;
import x10.zenfit.mvp.service.common.domain.enums.GoalPhaseStatus;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateClientGoalRequest {

    @NotBlank(message = "Client ID không được để trống")
    private String clientId;

    @NotBlank(message = "Trainer ID không được để trống")
    private String trainerId;

    @NotBlank(message = "Tên mục tiêu không được để trống")
    @Size(max = 100, message = "Tên mục tiêu không được vượt quá 100 ký tự")
    private String goalName;

    @NotBlank(message = "Loại mục tiêu không được để trống")
    @Size(max = 100, message = "Loại mục tiêu không được vượt quá 100 ký tự")
    private String goalType;

    @NotNull(message = "Ngày bắt đầu không được để trống")
    private LocalDate startDate;

    @NotNull(message = "Ngày kết thúc dự kiến không được để trống")
    private LocalDate endDate;

    @NotNull(message = "Cân nặng bắt đầu không được để trống")
    @DecimalMin(value = "1.0", message = "Cân nặng bắt đầu phải lớn hơn 0")
    @DecimalMax(value = "500.0", message = "Cân nặng bắt đầu không hợp lệ")
    private Double startWeight;

    @NotNull(message = "Cân nặng mục tiêu không được để trống")
    @DecimalMin(value = "1.0", message = "Cân nặng mục tiêu phải lớn hơn 0")
    @DecimalMax(value = "500.0", message = "Cân nặng mục tiêu không hợp lệ")
    private Double targetWeight;

    private GoalPhaseStatus status;

    @Size(max = 1000, message = "Ghi chú không được vượt quá 1000 ký tự")
    private String note;
}
