package x10.zenfit.mvp.service.service.workoutPlanService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.service.workoutPlanService.createPlanUc.ICreateWorkoutPlanUc;
import x10.zenfit.mvp.service.service.workoutPlanService.dayUc.IAddWorkoutDayUc;
import x10.zenfit.mvp.service.service.workoutPlanService.dayUc.IDeleteWorkoutDayUc;
import x10.zenfit.mvp.service.service.workoutPlanService.dayUc.IUpdateWorkoutDayUc;
import x10.zenfit.mvp.service.service.workoutPlanService.deletePlanUc.IDeleteWorkoutPlanUc;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.*;
import x10.zenfit.mvp.service.service.workoutPlanService.exerciseUc.IAddWorkoutExerciseUc;
import x10.zenfit.mvp.service.service.workoutPlanService.exerciseUc.IDeleteWorkoutExerciseUc;
import x10.zenfit.mvp.service.service.workoutPlanService.exerciseUc.IUpdateWorkoutExerciseUc;
import x10.zenfit.mvp.service.service.workoutPlanService.getClientPlansUc.IGetClientWorkoutPlansUc;
import x10.zenfit.mvp.service.service.workoutPlanService.getPlanDetailUc.IGetWorkoutPlanDetailUc;
import x10.zenfit.mvp.service.service.workoutPlanService.getPlansUc.IGetWorkoutPlansUc;
import x10.zenfit.mvp.service.service.workoutPlanService.updatePlanUc.IUpdateWorkoutPlanUc;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CoreWorkoutPlanServiceImpl implements ICoreWorkoutPlanService {

    private final ICreateWorkoutPlanUc createWorkoutPlanUc;
    private final IGetWorkoutPlansUc getWorkoutPlansUc;
    private final IGetWorkoutPlanDetailUc getWorkoutPlanDetailUc;
    private final IUpdateWorkoutPlanUc updateWorkoutPlanUc;
    private final IDeleteWorkoutPlanUc deleteWorkoutPlanUc;
    private final IGetClientWorkoutPlansUc getClientWorkoutPlansUc;
    private final IAddWorkoutDayUc addWorkoutDayUc;
    private final IUpdateWorkoutDayUc updateWorkoutDayUc;
    private final IDeleteWorkoutDayUc deleteWorkoutDayUc;
    private final IAddWorkoutExerciseUc addWorkoutExerciseUc;
    private final IUpdateWorkoutExerciseUc updateWorkoutExerciseUc;
    private final IDeleteWorkoutExerciseUc deleteWorkoutExerciseUc;

    @Override
    public WorkoutPlanResponse createPlan(String trainerId, CreateWorkoutPlanRequest request) {
        return createWorkoutPlanUc.createPlan(trainerId, request);
    }

    @Override
    public List<WorkoutPlanResponse> getTrainerPlans(String trainerId) {
        return getWorkoutPlansUc.getTrainerPlans(trainerId);
    }

    @Override
    public WorkoutPlanResponse getPlanDetail(String trainerId, String planId) {
        return getWorkoutPlanDetailUc.getPlanDetail(trainerId, planId);
    }

    @Override
    public WorkoutPlanResponse updatePlan(String trainerId, String planId, UpdateWorkoutPlanRequest request) {
        return updateWorkoutPlanUc.updatePlan(trainerId, planId, request);
    }

    @Override
    public void deletePlan(String trainerId, String planId) {
        deleteWorkoutPlanUc.deletePlan(trainerId, planId);
    }

    @Override
    public List<WorkoutPlanResponse> getClientPlans(String trainerId, String clientId) {
        return getClientWorkoutPlansUc.getClientPlans(trainerId, clientId);
    }

    @Override
    public WorkoutDayResponse addDay(String trainerId, String planId, WorkoutDayRequest request) {
        return addWorkoutDayUc.addDay(trainerId, planId, request);
    }

    @Override
    public WorkoutDayResponse updateDay(String trainerId, String planId, String dayId, UpdateWorkoutDayRequest request) {
        return updateWorkoutDayUc.updateDay(trainerId, planId, dayId, request);
    }

    @Override
    public void deleteDay(String trainerId, String planId, String dayId) {
        deleteWorkoutDayUc.deleteDay(trainerId, planId, dayId);
    }

    @Override
    public WorkoutExerciseResponse addExercise(String trainerId, String planId, String dayId, WorkoutExerciseRequest request) {
        return addWorkoutExerciseUc.addExercise(trainerId, planId, dayId, request);
    }

    @Override
    public WorkoutExerciseResponse updateExercise(String trainerId, String planId, String dayId, String exerciseId, UpdateWorkoutExerciseRequest request) {
        return updateWorkoutExerciseUc.updateExercise(trainerId, planId, dayId, exerciseId, request);
    }

    @Override
    public void deleteExercise(String trainerId, String planId, String dayId, String exerciseId) {
        deleteWorkoutExerciseUc.deleteExercise(trainerId, planId, dayId, exerciseId);
    }
}

