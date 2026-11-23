package x10.zenfit.mvp.service.service.workoutPlanService.getClientPlansUc;

import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutPlanResponse;

import java.util.List;

public interface IGetClientWorkoutPlansUc {
    List<WorkoutPlanResponse> getClientPlans(String trainerId, String clientId);
}

