package x10.zenfit.mvp.service.service.workoutPlanService.dayUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import x10.zenfit.mvp.service.Repository.IWorkoutPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutDayEntity;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutPlanEntity;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.UpdateWorkoutDayRequest;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutDayResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.workout.WorkoutPlanError;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateWorkoutDayUcImpl implements IUpdateWorkoutDayUc {

    private final IWorkoutPlanRepository workoutPlanRepository;

    @Override
    @Transactional
    public WorkoutDayResponse updateDay(
            String trainerId,
            String planId,
            String dayId,
            UpdateWorkoutDayRequest request
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
            throw new BusinessException(WorkoutPlanError.INVALID_DAY_DATA);
        }

        WorkoutPlanEntity plan = workoutPlanRepository.findById(planId.trim())
                .orElseThrow(() -> new BusinessException(WorkoutPlanError.PLAN_NOT_FOUND));

        if (!trainerId.trim().equals(plan.getTrainerId())) {
            log.warn("Trainer {} tried to update day {} in plan {} belonging to trainer {}",
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

        if (request.getDayNumber() != null) {
            if (request.getDayNumber() <= 0) {
                throw new BusinessException(WorkoutPlanError.INVALID_DAY_DATA);
            }
            targetDay.setDayNumber(request.getDayNumber());
        }

        if (request.getDayName() != null && !request.getDayName().isBlank()) {
            targetDay.setDayName(request.getDayName().trim());
        }

        if (request.getDescription() != null) {
            targetDay.setDescription(request.getDescription().trim());
        }

        plan.setUpdatedAt(LocalDateTime.now());
        workoutPlanRepository.save(plan);
        log.info("Updated workout day {} in plan {} by trainer {}", dayId, planId, trainerId);

        return WorkoutDayResponse.fromEntity(targetDay);
    }
}

