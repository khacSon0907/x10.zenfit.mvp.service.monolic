package x10.zenfit.mvp.service.service.workoutPlanService.exerciseUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import x10.zenfit.mvp.service.Repository.IWorkoutPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutDayEntity;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutPlanEntity;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.workout.WorkoutPlanError;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteWorkoutExerciseUcImpl implements IDeleteWorkoutExerciseUc {

    private final IWorkoutPlanRepository workoutPlanRepository;

    @Override
    @Transactional
    public void deleteExercise(String trainerId, String planId, String dayId, String exerciseId) {
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

        WorkoutPlanEntity plan = workoutPlanRepository.findById(planId.trim())
                .orElseThrow(() -> new BusinessException(WorkoutPlanError.PLAN_NOT_FOUND));

        if (!trainerId.trim().equals(plan.getTrainerId())) {
            log.warn("Trainer {} tried to delete exercise {} from day {} in plan {} belonging to trainer {}",
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

        boolean removed = targetDay.getExercises().removeIf(e -> exerciseId.trim().equals(e.getId()));
        if (!removed) {
            throw new BusinessException(WorkoutPlanError.EXERCISE_NOT_FOUND);
        }

        plan.setUpdatedAt(LocalDateTime.now());
        workoutPlanRepository.save(plan);
        log.info("Deleted exercise {} from day {} in plan {} by trainer {}",
                exerciseId, dayId, planId, trainerId);
    }
}

