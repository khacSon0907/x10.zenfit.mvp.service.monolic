package x10.zenfit.mvp.service.service.workoutPlanService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutDayEntity;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutDayResponse {

    private String id;
    private Integer dayNumber;
    private String dayName;
    private String description;

    @Builder.Default
    private List<WorkoutExerciseResponse> exercises = new ArrayList<>();

    public static WorkoutDayResponse fromEntity(WorkoutDayEntity entity) {
        if (entity == null) {
            return null;
        }

        List<WorkoutExerciseResponse> exerciseResponses = new ArrayList<>();
        if (entity.getExercises() != null) {
            exerciseResponses = entity.getExercises().stream()
                    .map(WorkoutExerciseResponse::fromEntity)
                    .toList();
        }

        return WorkoutDayResponse.builder()
                .id(entity.getId())
                .dayNumber(entity.getDayNumber())
                .dayName(entity.getDayName())
                .description(entity.getDescription())
                .exercises(exerciseResponses)
                .build();
    }
}

