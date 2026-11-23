package x10.zenfit.mvp.service.service.workoutPlanService.exerciseUc;

import x10.zenfit.mvp.service.service.workoutPlanService.dto.UpdateWorkoutExerciseRequest;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutExerciseResponse;

public interface IUpdateWorkoutExerciseUc {
    WorkoutExerciseResponse updateExercise(
            String trainerId,
            String planId,
            String dayId,
            String exerciseId,
            UpdateWorkoutExerciseRequest request
    );
}

