package x10.zenfit.mvp.service.service.workoutPlanService.dayUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import x10.zenfit.mvp.service.Repository.IWorkoutPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutPlanEntity;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.workout.WorkoutPlanError;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeleteWorkoutDayUcImpl implements IDeleteWorkoutDayUc {

    private final IWorkoutPlanRepository workoutPlanRepository;

    @Override
    @Transactional
    public void deleteDay(String trainerId, String planId, String dayId) {
        if (trainerId == null || trainerId.isBlank()) {
            throw new BusinessException(WorkoutPlanError.ACCESS_DENIED);
        }

        if (planId == null || planId.isBlank()) {
            throw new BusinessException(WorkoutPlanError.PLAN_NOT_FOUND);
        }

        if (dayId == null || dayId.isBlank()) {
            throw new BusinessException(WorkoutPlanError.DAY_NOT_FOUND);
        }

        WorkoutPlanEntity plan = workoutPlanRepository.findById(planId.trim())
                .orElseThrow(() -> new BusinessException(WorkoutPlanError.PLAN_NOT_FOUND));

        if (!trainerId.trim().equals(plan.getTrainerId())) {
            log.warn("Trainer {} tried to delete day {} in plan {} belonging to trainer {}",
                    trainerId, dayId, planId, plan.getTrainerId());
            throw new BusinessException(WorkoutPlanError.ACCESS_DENIED);
        }

        if (plan.getTrainingDays() == null || plan.getTrainingDays().isEmpty()) {
            throw new BusinessException(WorkoutPlanError.DAY_NOT_FOUND);
        }

        boolean removed = plan.getTrainingDays().removeIf(d -> dayId.trim().equals(d.getId()));
        if (!removed) {
            throw new BusinessException(WorkoutPlanError.DAY_NOT_FOUND);
        }

        plan.setUpdatedAt(LocalDateTime.now());
        workoutPlanRepository.save(plan);
        log.info("Deleted workout day {} from plan {} by trainer {}", dayId, planId, trainerId);
    }
}

