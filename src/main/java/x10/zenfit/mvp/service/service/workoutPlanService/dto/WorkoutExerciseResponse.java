package x10.zenfit.mvp.service.service.workoutPlanService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutExerciseEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExerciseResponse {

    private String id;
    private String exerciseName;
    private Integer sets;
    private Integer reps;
    private Integer restSeconds;
    private String tempo;
    private String note;

    public static WorkoutExerciseResponse fromEntity(WorkoutExerciseEntity entity) {
        if (entity == null) {
            return null;
        }

        return WorkoutExerciseResponse.builder()
                .id(entity.getId())
                .exerciseName(entity.getExerciseName())
                .sets(entity.getSets())
                .reps(entity.getReps())
                .restSeconds(entity.getRestSeconds())
                .tempo(entity.getTempo())
                .note(entity.getNote())
                .build();
    }
}

