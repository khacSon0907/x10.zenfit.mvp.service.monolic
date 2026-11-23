package x10.zenfit.mvp.service.service.workoutPlanService.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateWorkoutDayRequest {

    @Positive(message = "Thứ tự ngày tập (dayNumber) phải lớn hơn 0")
    private Integer dayNumber;

    private String dayName;

    private String description;
}

