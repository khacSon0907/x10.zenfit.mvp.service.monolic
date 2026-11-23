package x10.zenfit.mvp.service.service.workoutPlanService.dayUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import x10.zenfit.mvp.service.Repository.IWorkoutPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutDayEntity;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutExerciseEntity;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutPlanEntity;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutDayRequest;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutDayResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.workout.WorkoutPlanError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AddWorkoutDayUcImpl implements IAddWorkoutDayUc {

    private final IWorkoutPlanRepository workoutPlanRepository;

    @Override
    @Transactional
    public WorkoutDayResponse addDay(String trainerId, String planId, WorkoutDayRequest request) {
        if (trainerId == null || trainerId.isBlank()) {
            throw new BusinessException(WorkoutPlanError.ACCESS_DENIED);
        }

        if (planId == null || planId.isBlank()) {
            throw new BusinessException(WorkoutPlanError.PLAN_NOT_FOUND);
        }

        if (request == null) {
            throw new BusinessException(WorkoutPlanError.INVALID_DAY_DATA);
        }

        WorkoutPlanEntity plan = workoutPlanRepository.findById(planId.trim())
                .orElseThrow(() -> new BusinessException(WorkoutPlanError.PLAN_NOT_FOUND));

        if (!trainerId.trim().equals(plan.getTrainerId())) {
            log.warn("Trainer {} tried to add day to plan {} belonging to trainer {}",
                    trainerId, planId, plan.getTrainerId());
            throw new BusinessException(WorkoutPlanError.ACCESS_DENIED);
        }

        if (request.getDayNumber() == null || request.getDayNumber() <= 0) {
            throw new BusinessException(WorkoutPlanError.INVALID_DAY_DATA);
        }

        List<WorkoutExerciseEntity> exercises = new ArrayList<>();
        if (request.getExercises() != null && !request.getExercises().isEmpty()) {
            for (var exReq : request.getExercises()) {
                exercises.add(WorkoutExerciseEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .exerciseName(exReq.getExerciseName().trim())
                        .sets(exReq.getSets())
                        .reps(exReq.getReps())
                        .restSeconds(exReq.getRestSeconds())
                        .tempo(exReq.getTempo().trim())
                        .note(exReq.getNote())
                        .build());
            }
        }

        WorkoutDayEntity newDay = WorkoutDayEntity.builder()
                .id(UUID.randomUUID().toString())
                .dayNumber(request.getDayNumber())
                .dayName(request.getDayName().trim())
                .description(request.getDescription())
                .exercises(exercises)
                .build();

        if (plan.getTrainingDays() == null) {
            plan.setTrainingDays(new ArrayList<>());
        }
        plan.getTrainingDays().add(newDay);
        plan.setUpdatedAt(LocalDateTime.now());

        workoutPlanRepository.save(plan);
        log.info("Added workout day {} to plan {} by trainer {}", newDay.getId(), planId, trainerId);

        return WorkoutDayResponse.fromEntity(newDay);
    }
}

