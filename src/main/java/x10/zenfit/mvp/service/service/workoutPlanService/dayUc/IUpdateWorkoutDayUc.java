package x10.zenfit.mvp.service.service.workoutPlanService.dayUc;

import x10.zenfit.mvp.service.service.workoutPlanService.dto.UpdateWorkoutDayRequest;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutDayResponse;

public interface IUpdateWorkoutDayUc {
    WorkoutDayResponse updateDay(String trainerId, String planId, String dayId, UpdateWorkoutDayRequest request);
}

