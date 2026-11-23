package x10.zenfit.mvp.service.service.workoutPlanService.createPlanUc;

import x10.zenfit.mvp.service.service.workoutPlanService.dto.CreateWorkoutPlanRequest;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutPlanResponse;

public interface ICreateWorkoutPlanUc {
    WorkoutPlanResponse createPlan(String trainerId, CreateWorkoutPlanRequest request);
}

