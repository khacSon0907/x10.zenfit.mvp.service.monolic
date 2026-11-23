package x10.zenfit.mvp.service.service.workoutPlanService.getPlanDetailUc;

import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutPlanResponse;

public interface IGetWorkoutPlanDetailUc {
    WorkoutPlanResponse getPlanDetail(String trainerId, String planId);
}

