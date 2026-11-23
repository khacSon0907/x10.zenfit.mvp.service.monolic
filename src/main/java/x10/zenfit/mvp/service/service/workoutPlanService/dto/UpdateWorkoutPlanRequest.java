package x10.zenfit.mvp.service.service.workoutPlanService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.WorkoutPlanStatus;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWorkoutPlanRequest {

    private String name;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private WorkoutPlanStatus status;
}

