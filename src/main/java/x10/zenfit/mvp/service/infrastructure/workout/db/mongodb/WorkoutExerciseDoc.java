package x10.zenfit.mvp.service.infrastructure.workout.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExerciseDoc {

    private String id;
    private String exerciseName;
    private Integer sets;
    private Integer reps;
    private Integer restSeconds;
    private String tempo;
    private String note;
}

