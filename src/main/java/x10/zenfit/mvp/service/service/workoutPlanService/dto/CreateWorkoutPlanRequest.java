package x10.zenfit.mvp.service.service.workoutPlanService.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.WorkoutPlanStatus;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateWorkoutPlanRequest {

    @NotBlank(message = "Mã khách hàng (clientId) không được để trống")
    private String clientId;

    @NotBlank(message = "Tên giáo án không được để trống")
    private String name;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private WorkoutPlanStatus status;

    @Valid
    private List<WorkoutDayRequest> trainingDays;
}

