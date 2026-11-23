package x10.zenfit.mvp.service.service.workoutPlanService;

import x10.zenfit.mvp.service.service.workoutPlanService.dto.*;

import java.util.List;

public interface ICoreWorkoutPlanService {

    WorkoutPlanResponse createPlan(String trainerId, CreateWorkoutPlanRequest request);

    List<WorkoutPlanResponse> getTrainerPlans(String trainerId);

    WorkoutPlanResponse getPlanDetail(String trainerId, String planId);

    WorkoutPlanResponse updatePlan(String trainerId, String planId, UpdateWorkoutPlanRequest request);

    void deletePlan(String trainerId, String planId);

    List<WorkoutPlanResponse> getClientPlans(String trainerId, String clientId);

    WorkoutDayResponse addDay(String trainerId, String planId, WorkoutDayRequest request);

    WorkoutDayResponse updateDay(String trainerId, String planId, String dayId, UpdateWorkoutDayRequest request);

    void deleteDay(String trainerId, String planId, String dayId);

    WorkoutExerciseResponse addExercise(String trainerId, String planId, String dayId, WorkoutExerciseRequest request);

    WorkoutExerciseResponse updateExercise(String trainerId, String planId, String dayId, String exerciseId, UpdateWorkoutExerciseRequest request);

    void deleteExercise(String trainerId, String planId, String dayId, String exerciseId);
}

