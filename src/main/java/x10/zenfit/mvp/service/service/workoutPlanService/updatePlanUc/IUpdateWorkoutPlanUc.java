package x10.zenfit.mvp.service.service.workoutPlanService.updatePlanUc;

import x10.zenfit.mvp.service.service.workoutPlanService.dto.UpdateWorkoutPlanRequest;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutPlanResponse;

public interface IUpdateWorkoutPlanUc {
    WorkoutPlanResponse updatePlan(String trainerId, String planId, UpdateWorkoutPlanRequest request);
}

