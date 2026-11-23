package x10.zenfit.mvp.service.service.workoutPlanService.exerciseUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import x10.zenfit.mvp.service.Repository.IWorkoutPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutDayEntity;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutExerciseEntity;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutPlanEntity;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.UpdateWorkoutExerciseRequest;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutExerciseResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.workout.WorkoutPlanError;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateWorkoutExerciseUcImpl implements IUpdateWorkoutExerciseUc {

    private final IWorkoutPlanRepository workoutPlanRepository;

    @Override
    @Transactional
    public WorkoutExerciseResponse updateExercise(
            String trainerId,
            String planId,
            String dayId,
            String exerciseId,
            UpdateWorkoutExerciseRequest request
    ) {
        if (trainerId == null || trainerId.isBlank()) {
            throw new BusinessException(WorkoutPlanError.ACCESS_DENIED);
        }

        if (planId == null || planId.isBlank()) {
            throw new BusinessException(WorkoutPlanError.PLAN_NOT_FOUND);
        }

        if (dayId == null || dayId.isBlank()) {
            throw new BusinessException(WorkoutPlanError.DAY_NOT_FOUND);
        }

        if (exerciseId == null || exerciseId.isBlank()) {
            throw new BusinessException(WorkoutPlanError.EXERCISE_NOT_FOUND);
        }

        if (request == null) {
            throw new BusinessException(WorkoutPlanError.INVALID_EXERCISE_DATA);
        }

        WorkoutPlanEntity plan = workoutPlanRepository.findById(planId.trim())
                .orElseThrow(() -> new BusinessException(WorkoutPlanError.PLAN_NOT_FOUND));

        if (!trainerId.trim().equals(plan.getTrainerId())) {
            log.warn("Trainer {} tried to update exercise {} in day {} of plan {} belonging to trainer {}",
                    trainerId, exerciseId, dayId, planId, plan.getTrainerId());
            throw new BusinessException(WorkoutPlanError.ACCESS_DENIED);
        }

        if (plan.getTrainingDays() == null || plan.getTrainingDays().isEmpty()) {
            throw new BusinessException(WorkoutPlanError.DAY_NOT_FOUND);
        }

        WorkoutDayEntity targetDay = plan.getTrainingDays().stream()
                .filter(d -> dayId.trim().equals(d.getId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(WorkoutPlanError.DAY_NOT_FOUND));

        if (targetDay.getExercises() == null || targetDay.getExercises().isEmpty()) {
            throw new BusinessException(WorkoutPlanError.EXERCISE_NOT_FOUND);
        }

        WorkoutExerciseEntity targetExercise = targetDay.getExercises().stream()
                .filter(e -> exerciseId.trim().equals(e.getId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(WorkoutPlanError.EXERCISE_NOT_FOUND));

        if (request.getExerciseName() != null) {
            if (request.getExerciseName().isBlank()) {
                throw new BusinessException(WorkoutPlanError.INVALID_EXERCISE_DATA);
            }
            targetExercise.setExerciseName(request.getExerciseName().trim());
        }

        if (request.getSets() != null) {
            if (request.getSets() <= 0) {
                throw new BusinessException(WorkoutPlanError.INVALID_EXERCISE_DATA);
            }
            targetExercise.setSets(request.getSets());
        }

        if (request.getReps() != null) {
            if (request.getReps() <= 0) {
                throw new BusinessException(WorkoutPlanError.INVALID_EXERCISE_DATA);
            }
            targetExercise.setReps(request.getReps());
        }

        if (request.getRestSeconds() != null) {
            if (request.getRestSeconds() < 0) {
                throw new BusinessException(WorkoutPlanError.INVALID_EXERCISE_DATA);
            }
            targetExercise.setRestSeconds(request.getRestSeconds());
        }

        if (request.getTempo() != null) {
            if (request.getTempo().isBlank()) {
                throw new BusinessException(WorkoutPlanError.INVALID_EXERCISE_DATA);
            }
            targetExercise.setTempo(request.getTempo().trim());
        }

        if (request.getNote() != null) {
            targetExercise.setNote(request.getNote().trim());
        }

        plan.setUpdatedAt(LocalDateTime.now());
        workoutPlanRepository.save(plan);
        log.info("Updated exercise {} in day {} of plan {} by trainer {}",
                exerciseId, dayId, planId, trainerId);

        return WorkoutExerciseResponse.fromEntity(targetExercise);
    }
}

