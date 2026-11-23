package x10.zenfit.mvp.service.service.workoutPlanService.exerciseUc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import x10.zenfit.mvp.service.Repository.IWorkoutPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutDayEntity;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutExerciseEntity;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutPlanEntity;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.UpdateWorkoutExerciseRequest;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutExerciseRequest;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutExerciseResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.workout.WorkoutPlanError;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WorkoutExerciseUcImplTest {

    private IWorkoutPlanRepository workoutPlanRepository;
    private AddWorkoutExerciseUcImpl addWorkoutExerciseUc;
    private UpdateWorkoutExerciseUcImpl updateWorkoutExerciseUc;
    private DeleteWorkoutExerciseUcImpl deleteWorkoutExerciseUc;

    @BeforeEach
    void setUp() {
        workoutPlanRepository = mock(IWorkoutPlanRepository.class);
        addWorkoutExerciseUc = new AddWorkoutExerciseUcImpl(workoutPlanRepository);
        updateWorkoutExerciseUc = new UpdateWorkoutExerciseUcImpl(workoutPlanRepository);
        deleteWorkoutExerciseUc = new DeleteWorkoutExerciseUcImpl(workoutPlanRepository);
    }

    @Test
    @DisplayName("Thêm bài tập vào ngày tập thành công")
    void addExercise_Success() {
        String trainerId = "pt-123";
        String planId = "plan-001";
        String dayId = "day-001";

        WorkoutDayEntity day = WorkoutDayEntity.builder()
                .id(dayId)
                .dayNumber(1)
                .dayName("Day 1: Chest")
                .exercises(new ArrayList<>())
                .build();

        WorkoutPlanEntity plan = WorkoutPlanEntity.builder()
                .id(planId)
                .trainerId(trainerId)
                .trainingDays(new ArrayList<>(List.of(day)))
                .build();

        when(workoutPlanRepository.findById(planId)).thenReturn(Optional.of(plan));
        when(workoutPlanRepository.save(any(WorkoutPlanEntity.class))).thenAnswer(i -> i.getArgument(0));

        WorkoutExerciseRequest request = WorkoutExerciseRequest.builder()
                .exerciseName("Incline Dumbbell Press")
                .sets(4)
                .reps(10)
                .restSeconds(60)
                .tempo("2-0-1-0")
                .note("Nâng góc ghế 30 độ")
                .build();

        WorkoutExerciseResponse response = addWorkoutExerciseUc.addExercise(trainerId, planId, dayId, request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotBlank();
        assertThat(response.getExerciseName()).isEqualTo("Incline Dumbbell Press");
        assertThat(response.getSets()).isEqualTo(4);
        assertThat(response.getReps()).isEqualTo(10);
        assertThat(response.getRestSeconds()).isEqualTo(60);
        assertThat(response.getTempo()).isEqualTo("2-0-1-0");
        assertThat(day.getExercises()).hasSize(1);
        verify(workoutPlanRepository, times(1)).save(plan);
    }

    @Test
    @DisplayName("Cập nhật bài tập thành công")
    void updateExercise_Success() {
        String trainerId = "pt-123";
        String planId = "plan-001";
        String dayId = "day-001";
        String exerciseId = "ex-001";

        WorkoutExerciseEntity exercise = WorkoutExerciseEntity.builder()
                .id(exerciseId)
                .exerciseName("Squat")
                .sets(3)
                .reps(12)
                .restSeconds(60)
                .tempo("3-0-1-0")
                .build();

        WorkoutDayEntity day = WorkoutDayEntity.builder()
                .id(dayId)
                .exercises(new ArrayList<>(List.of(exercise)))
                .build();

        WorkoutPlanEntity plan = WorkoutPlanEntity.builder()
                .id(planId)
                .trainerId(trainerId)
                .trainingDays(new ArrayList<>(List.of(day)))
                .build();

        when(workoutPlanRepository.findById(planId)).thenReturn(Optional.of(plan));

        UpdateWorkoutExerciseRequest request = UpdateWorkoutExerciseRequest.builder()
                .sets(5)
                .reps(5)
                .restSeconds(120)
                .note("Tăng mức tạ lên 100kg")
                .build();

        WorkoutExerciseResponse response = updateWorkoutExerciseUc.updateExercise(
                trainerId, planId, dayId, exerciseId, request);

        assertThat(response).isNotNull();
        assertThat(response.getSets()).isEqualTo(5);
        assertThat(response.getReps()).isEqualTo(5);
        assertThat(response.getRestSeconds()).isEqualTo(120);
        assertThat(response.getExerciseName()).isEqualTo("Squat");
        assertThat(response.getNote()).isEqualTo("Tăng mức tạ lên 100kg");
        verify(workoutPlanRepository, times(1)).save(plan);
    }

    @Test
    @DisplayName("Xóa bài tập thành công")
    void deleteExercise_Success() {
        String trainerId = "pt-123";
        String planId = "plan-001";
        String dayId = "day-001";
        String exerciseId = "ex-001";

        WorkoutExerciseEntity exercise = WorkoutExerciseEntity.builder()
                .id(exerciseId)
                .exerciseName("Deadlift")
                .build();

        WorkoutDayEntity day = WorkoutDayEntity.builder()
                .id(dayId)
                .exercises(new ArrayList<>(List.of(exercise)))
                .build();

        WorkoutPlanEntity plan = WorkoutPlanEntity.builder()
                .id(planId)
                .trainerId(trainerId)
                .trainingDays(new ArrayList<>(List.of(day)))
                .build();

        when(workoutPlanRepository.findById(planId)).thenReturn(Optional.of(plan));

        deleteWorkoutExerciseUc.deleteExercise(trainerId, planId, dayId, exerciseId);

        assertThat(day.getExercises()).isEmpty();
        verify(workoutPlanRepository, times(1)).save(plan);
    }

    @Test
    @DisplayName("Ném EXERCISE_NOT_FOUND khi exerciseId không tồn tại trong buổi tập")
    void deleteExercise_NotFound() {
        String trainerId = "pt-123";
        String planId = "plan-001";
        String dayId = "day-001";

        WorkoutDayEntity day = WorkoutDayEntity.builder()
                .id(dayId)
                .exercises(new ArrayList<>())
                .build();

        WorkoutPlanEntity plan = WorkoutPlanEntity.builder()
                .id(planId)
                .trainerId(trainerId)
                .trainingDays(new ArrayList<>(List.of(day)))
                .build();

        when(workoutPlanRepository.findById(planId)).thenReturn(Optional.of(plan));

        assertThatThrownBy(() -> deleteWorkoutExerciseUc.deleteExercise(trainerId, planId, dayId, "non-existent"))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> {
                    BusinessException be = (BusinessException) ex;
                    assertThat(be.getError()).isEqualTo(WorkoutPlanError.EXERCISE_NOT_FOUND);
                });
    }
}

