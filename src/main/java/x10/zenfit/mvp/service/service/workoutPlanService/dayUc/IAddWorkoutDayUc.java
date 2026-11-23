package x10.zenfit.mvp.service.service.workoutPlanService.dayUc;

import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutDayRequest;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutDayResponse;

public interface IAddWorkoutDayUc {
    WorkoutDayResponse addDay(String trainerId, String planId, WorkoutDayRequest request);
}

