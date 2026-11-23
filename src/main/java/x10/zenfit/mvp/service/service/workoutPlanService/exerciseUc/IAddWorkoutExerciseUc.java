package x10.zenfit.mvp.service.service.workoutPlanService.exerciseUc;

import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutExerciseRequest;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutExerciseResponse;

public interface IAddWorkoutExerciseUc {
    WorkoutExerciseResponse addExercise(
            String trainerId,
            String planId,
            String dayId,
            WorkoutExerciseRequest request
    );
}

