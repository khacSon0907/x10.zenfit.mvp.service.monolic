package x10.zenfit.mvp.service.service.workoutPlanService.getPlansUc;

import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutPlanResponse;

import java.util.List;

public interface IGetWorkoutPlansUc {
    List<WorkoutPlanResponse> getTrainerPlans(String trainerId);
}

