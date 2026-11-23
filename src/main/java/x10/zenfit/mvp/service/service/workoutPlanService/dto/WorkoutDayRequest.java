package x10.zenfit.mvp.service.service.workoutPlanService.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutDayRequest {

    @NotNull(message = "Thứ tự ngày tập (dayNumber) không được để trống")
    @Positive(message = "Thứ tự ngày tập (dayNumber) phải lớn hơn 0")
    private Integer dayNumber;

    @NotBlank(message = "Tên buổi tập (dayName) không được để trống")
    private String dayName;

    private String description;

    @Valid
    private List<WorkoutExerciseRequest> exercises;
}

