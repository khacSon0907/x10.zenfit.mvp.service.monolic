package x10.zenfit.mvp.service.service.workoutPlanService.createPlanUc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.Repository.IWorkoutPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutDayEntity;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutExerciseEntity;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutPlanEntity;
import x10.zenfit.mvp.service.common.domain.enums.WorkoutPlanStatus;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.CreateWorkoutPlanRequest;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutPlanResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.workout.WorkoutPlanError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class CreateWorkoutPlanUcImpl implements ICreateWorkoutPlanUc {

    private final IWorkoutPlanRepository workoutPlanRepository;
    private final IClientRepository clientRepository;

    @Override
    @Transactional
    public WorkoutPlanResponse createPlan(String trainerId, CreateWorkoutPlanRequest request) {
        if (trainerId == null || trainerId.isBlank()) {
            throw new BusinessException(WorkoutPlanError.ACCESS_DENIED);
        }

        if (request == null) {
            throw new BusinessException(WorkoutPlanError.INVALID_DATA);
        }

        // 1. Kiểm tra ngày hợp lệ
        if (request.getStartDate() != null && request.getEndDate() != null) {
            if (request.getStartDate().isAfter(request.getEndDate())) {
                throw new BusinessException(WorkoutPlanError.INVALID_DATE_RANGE);
            }
        }

        // 2. Kiểm tra Client tồn tại
        ClientEntity client = clientRepository.findById(request.getClientId().trim())
                .orElseThrow(() -> new BusinessException(WorkoutPlanError.CLIENT_NOT_FOUND));

        // 3. Kiểm tra Client có thuộc quyền PT đang đăng nhập không
        if (client.getAssignedTrainerId() == null || !client.getAssignedTrainerId().equals(trainerId.trim())) {
            log.warn("Trainer {} tried to create workout plan for client {} assigned to {}",
                    trainerId, client.getId(), client.getAssignedTrainerId());
            throw new BusinessException(WorkoutPlanError.CLIENT_NOT_ASSIGNED_TO_TRAINER);
        }

        // 4. Khởi tạo danh sách ngày tập (nếu có truyền vào)
        List<WorkoutDayEntity> days = new ArrayList<>();
        if (request.getTrainingDays() != null && !request.getTrainingDays().isEmpty()) {
            for (var dayReq : request.getTrainingDays()) {
                List<WorkoutExerciseEntity> exercises = new ArrayList<>();
                if (dayReq.getExercises() != null) {
                    for (var exReq : dayReq.getExercises()) {
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

                days.add(WorkoutDayEntity.builder()
                        .id(UUID.randomUUID().toString())
                        .dayNumber(dayReq.getDayNumber())
                        .dayName(dayReq.getDayName().trim())
                        .description(dayReq.getDescription())
                        .exercises(exercises)
                        .build());
            }
        }

        LocalDateTime now = LocalDateTime.now();
        WorkoutPlanEntity plan = WorkoutPlanEntity.builder()
                .trainerId(trainerId.trim())
                .clientId(client.getId())
                .name(request.getName().trim())
                .description(request.getDescription())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .status(request.getStatus() != null ? request.getStatus() : WorkoutPlanStatus.ACTIVE)
                .trainingDays(days)
                .createdAt(now)
                .updatedAt(now)
                .build();

        WorkoutPlanEntity saved = workoutPlanRepository.save(plan);
        log.info("Created workout plan {} for client {} by trainer {}", saved.getId(), client.getId(), trainerId);

        return WorkoutPlanResponse.fromEntity(saved, client.getFullName());
    }
}

