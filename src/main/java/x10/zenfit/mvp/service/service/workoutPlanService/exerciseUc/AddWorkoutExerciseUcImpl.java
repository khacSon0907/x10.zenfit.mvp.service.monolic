package x10.zenfit.mvp.service.service.workoutPlanService.exerciseUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import x10.zenfit.mvp.service.Repository.IWorkoutPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutDayEntity;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutExerciseEntity;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutPlanEntity;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutExerciseRequest;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutExerciseResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.workout.WorkoutPlanError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddWorkoutExerciseUcImpl implements IAddWorkoutExerciseUc {

    private final IWorkoutPlanRepository workoutPlanRepository;

    @Override
    @Transactional
    public WorkoutExerciseResponse addExercise(
            String trainerId,
            String planId,
            String dayId,
            WorkoutExerciseRequest request
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

        if (request == null) {
            throw new BusinessException(WorkoutPlanError.INVALID_EXERCISE_DATA);
        }

        WorkoutPlanEntity plan = workoutPlanRepository.findById(planId.trim())
                .orElseThrow(() -> new BusinessException(WorkoutPlanError.PLAN_NOT_FOUND));

        if (!trainerId.trim().equals(plan.getTrainerId())) {
            log.warn("Trainer {} tried to add exercise to day {} in plan {} belonging to trainer {}",
                    trainerId, dayId, planId, plan.getTrainerId());
            throw new BusinessException(WorkoutPlanError.ACCESS_DENIED);
        }

        if (plan.getTrainingDays() == null || plan.getTrainingDays().isEmpty()) {
            throw new BusinessException(WorkoutPlanError.DAY_NOT_FOUND);
        }

        WorkoutDayEntity targetDay = plan.getTrainingDays().stream()
                .filter(d -> dayId.trim().equals(d.getId()))
                .findFirst()
                .orElseThrow(() -> new BusinessException(WorkoutPlanError.DAY_NOT_FOUND));

        if (request.getExerciseName() == null || request.getExerciseName().isBlank()
                || request.getSets() == null || request.getSets() <= 0
                || request.getReps() == null || request.getReps() <= 0
                || request.getRestSeconds() == null || request.getRestSeconds() < 0
                || request.getTempo() == null || request.getTempo().isBlank()) {
            throw new BusinessException(WorkoutPlanError.INVALID_EXERCISE_DATA);
        }

        WorkoutExerciseEntity newExercise = WorkoutExerciseEntity.builder()
                .id(UUID.randomUUID().toString())
                .exerciseName(request.getExerciseName().trim())
                .sets(request.getSets())
                .reps(request.getReps())
                .restSeconds(request.getRestSeconds())
                .tempo(request.getTempo().trim())
                .note(request.getNote() != null ? request.getNote().trim() : null)
                .build();

        if (targetDay.getExercises() == null) {
            targetDay.setExercises(new ArrayList<>());
        }
        targetDay.getExercises().add(newExercise);
        plan.setUpdatedAt(LocalDateTime.now());

        workoutPlanRepository.save(plan);
        log.info("Added exercise {} to day {} in plan {} by trainer {}",
                newExercise.getId(), dayId, planId, trainerId);

        return WorkoutExerciseResponse.fromEntity(newExercise);
    }
}

