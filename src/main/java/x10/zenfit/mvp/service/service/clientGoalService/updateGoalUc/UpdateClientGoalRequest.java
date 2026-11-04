package x10.zenfit.mvp.service.service.clientGoalService.updateGoalUc;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.GoalPhaseStatus;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateClientGoalRequest {

    private String trainerId;

    @Size(max = 100, message = "Tên mục tiêu không được vượt quá 100 ký tự")
    private String goalName;

    @Size(max = 100, message = "Loại mục tiêu không được vượt quá 100 ký tự")
    private String goalType;

    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate actualEndDate;

    @DecimalMin(value = "1.0", message = "Cân nặng bắt đầu phải lớn hơn 0")
    @DecimalMax(value = "500.0", message = "Cân nặng bắt đầu không hợp lệ")
    private Double startWeight;

    @DecimalMin(value = "1.0", message = "Cân nặng mục tiêu phải lớn hơn 0")
    @DecimalMax(value = "500.0", message = "Cân nặng mục tiêu không hợp lệ")
    private Double targetWeight;

    @DecimalMin(value = "1.0", message = "Cân nặng thực tế kết thúc phải lớn hơn 0")
    @DecimalMax(value = "500.0", message = "Cân nặng thực tế kết thúc không hợp lệ")
    private Double endWeight;

    private GoalPhaseStatus status;

    @Size(max = 1000, message = "Ghi chú không được vượt quá 1000 ký tự")
    private String note;
}

