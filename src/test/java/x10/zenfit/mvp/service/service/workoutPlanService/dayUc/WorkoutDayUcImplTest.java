package x10.zenfit.mvp.service.service.workoutPlanService.dayUc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import x10.zenfit.mvp.service.Repository.IWorkoutPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutDayEntity;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutPlanEntity;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.UpdateWorkoutDayRequest;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutDayRequest;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutDayResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.workout.WorkoutPlanError;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class WorkoutDayUcImplTest {

    private IWorkoutPlanRepository workoutPlanRepository;
    private AddWorkoutDayUcImpl addWorkoutDayUc;
    private UpdateWorkoutDayUcImpl updateWorkoutDayUc;
    private DeleteWorkoutDayUcImpl deleteWorkoutDayUc;

    @BeforeEach
    void setUp() {
        workoutPlanRepository = mock(IWorkoutPlanRepository.class);
        addWorkoutDayUc = new AddWorkoutDayUcImpl(workoutPlanRepository);
        updateWorkoutDayUc = new UpdateWorkoutDayUcImpl(workoutPlanRepository);
        deleteWorkoutDayUc = new DeleteWorkoutDayUcImpl(workoutPlanRepository);
    }

    @Test
    @DisplayName("Thêm ngày tập vào giáo án thành công")
    void addDay_Success() {
        String trainerId = "pt-123";
        String planId = "plan-001";

        WorkoutPlanEntity plan = WorkoutPlanEntity.builder()
                .id(planId)
                .trainerId(trainerId)
                .trainingDays(new ArrayList<>())
                .build();

        when(workoutPlanRepository.findById(planId)).thenReturn(Optional.of(plan));
        when(workoutPlanRepository.save(any(WorkoutPlanEntity.class))).thenAnswer(i -> i.getArgument(0));

        WorkoutDayRequest request = WorkoutDayRequest.builder()
                .dayNumber(1)
                .dayName("Thứ 2: Ngực - Vai - Tay sau")
                .description("Buổi tập thân trên")
                .build();

        WorkoutDayResponse response = addWorkoutDayUc.addDay(trainerId, planId, request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotBlank();
        assertThat(response.getDayNumber()).isEqualTo(1);
        assertThat(response.getDayName()).isEqualTo("Thứ 2: Ngực - Vai - Tay sau");
        assertThat(plan.getTrainingDays()).hasSize(1);
        verify(workoutPlanRepository, times(1)).save(plan);
    }

    @Test
    @DisplayName("Cập nhật ngày tập thành công")
    void updateDay_Success() {
        String trainerId = "pt-123";
        String planId = "plan-001";
        String dayId = "day-001";

        WorkoutDayEntity existingDay = WorkoutDayEntity.builder()
                .id(dayId)
                .dayNumber(1)
                .dayName("Day 1: Leg")
                .build();

        WorkoutPlanEntity plan = WorkoutPlanEntity.builder()
                .id(planId)
                .trainerId(trainerId)
                .trainingDays(new ArrayList<>(List.of(existingDay)))
                .build();

        when(workoutPlanRepository.findById(planId)).thenReturn(Optional.of(plan));
        when(workoutPlanRepository.save(any(WorkoutPlanEntity.class))).thenAnswer(i -> i.getArgument(0));

        UpdateWorkoutDayRequest request = UpdateWorkoutDayRequest.builder()
                .dayName("Day 1: Heavy Legs & Core")
                .description("Đổi giáo án tập chân nặng")
                .build();

        WorkoutDayResponse response = updateWorkoutDayUc.updateDay(trainerId, planId, dayId, request);

        assertThat(response).isNotNull();
        assertThat(response.getDayName()).isEqualTo("Day 1: Heavy Legs & Core");
        assertThat(response.getDescription()).isEqualTo("Đổi giáo án tập chân nặng");
        verify(workoutPlanRepository, times(1)).save(plan);
    }

    @Test
    @DisplayName("Xóa ngày tập thành công")
    void deleteDay_Success() {
        String trainerId = "pt-123";
        String planId = "plan-001";
        String dayId = "day-001";

        WorkoutDayEntity day = WorkoutDayEntity.builder()
                .id(dayId)
                .dayNumber(1)
                .dayName("Day 1")
                .build();

        WorkoutPlanEntity plan = WorkoutPlanEntity.builder()
                .id(planId)
                .trainerId(trainerId)
                .trainingDays(new ArrayList<>(List.of(day)))
                .build();

        when(workoutPlanRepository.findById(planId)).thenReturn(Optional.of(plan));

        deleteWorkoutDayUc.deleteDay(trainerId, planId, dayId);

        assertThat(plan.getTrainingDays()).isEmpty();
        verify(workoutPlanRepository, times(1)).save(plan);
    }

    @Test
    @DisplayName("Ném ACCESS_DENIED khi PT khác cố gắng sửa ngày tập")
    void updateDay_AccessDenied() {
        String trainerId = "pt-123";
        String planId = "plan-001";
        String dayId = "day-001";

        WorkoutPlanEntity plan = WorkoutPlanEntity.builder()
                .id(planId)
                .trainerId("other-pt")
                .trainingDays(new ArrayList<>())
                .build();

        when(workoutPlanRepository.findById(planId)).thenReturn(Optional.of(plan));

        UpdateWorkoutDayRequest request = UpdateWorkoutDayRequest.builder()
                .dayName("Test")
                .build();

        assertThatThrownBy(() -> updateWorkoutDayUc.updateDay(trainerId, planId, dayId, request))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> {
                    BusinessException be = (BusinessException) ex;
                    assertThat(be.getError()).isEqualTo(WorkoutPlanError.ACCESS_DENIED);
                });
    }
}

