package x10.zenfit.mvp.service.service.workoutPlanService.createPlanUc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.Repository.IWorkoutPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.common.domain.entities.workout.WorkoutPlanEntity;
import x10.zenfit.mvp.service.common.domain.enums.WorkoutPlanStatus;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.CreateWorkoutPlanRequest;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutDayRequest;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutExerciseRequest;
import x10.zenfit.mvp.service.service.workoutPlanService.dto.WorkoutPlanResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.workout.WorkoutPlanError;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateWorkoutPlanUcImplTest {

    private IWorkoutPlanRepository workoutPlanRepository;
    private IClientRepository clientRepository;
    private CreateWorkoutPlanUcImpl useCase;

    @BeforeEach
    void setUp() {
        workoutPlanRepository = mock(IWorkoutPlanRepository.class);
        clientRepository = mock(IClientRepository.class);
        useCase = new CreateWorkoutPlanUcImpl(workoutPlanRepository, clientRepository);
    }

    @Test
    @DisplayName("Tạo giáo án tập Gym thành công cho Client thuộc quyền quản lý của PT (ví dụ lịch 3 buổi)")
    void createPlan_Success_With3DaySplit() {
        String trainerId = "pt-123";
        String clientId = "client-456";

        ClientEntity client = ClientEntity.builder()
                .id(clientId)
                .fullName("Nguyễn Văn A")
                .assignedTrainerId(trainerId)
                .build();

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(workoutPlanRepository.save(any(WorkoutPlanEntity.class))).thenAnswer(invocation -> {
            WorkoutPlanEntity entity = invocation.getArgument(0);
            entity.setId("plan-001");
            return entity;
        });

        // 3-Day Split (Push - Pull - Legs)
        WorkoutDayRequest pushDay = WorkoutDayRequest.builder()
                .dayNumber(1)
                .dayName("Day 1: Push (Ngực, Vai, Tay sau)")
                .description("Buổi tập đẩy tập trung cơ ngực và vai")
                .exercises(List.of(
                        WorkoutExerciseRequest.builder()
                                .exerciseName("Barbell Bench Press")
                                .sets(4)
                                .reps(8)
                                .restSeconds(90)
                                .tempo("3-0-1-0")
                                .note("Xuống chậm, siết đỉnh")
                                .build()
                ))
                .build();

        WorkoutDayRequest pullDay = WorkoutDayRequest.builder()
                .dayNumber(2)
                .dayName("Day 2: Pull (Lưng, Tay trước)")
                .description("Buổi tập kéo tập trung cơ lưng xô")
                .build();

        WorkoutDayRequest legsDay = WorkoutDayRequest.builder()
                .dayNumber(3)
                .dayName("Day 3: Legs (Chân, Bắp chân, Bụng)")
                .description("Buổi tập chân")
                .build();

        CreateWorkoutPlanRequest request = CreateWorkoutPlanRequest.builder()
                .clientId(clientId)
                .name("Giáo án Tăng cơ 3 buổi/tuần")
                .description("Lịch tập Push-Pull-Legs cho học viên mới")
                .startDate(LocalDate.of(2026, 12, 1))
                .endDate(LocalDate.of(2026, 12, 31))
                .trainingDays(List.of(pushDay, pullDay, legsDay))
                .build();

        WorkoutPlanResponse response = useCase.createPlan(trainerId, request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo("plan-001");
        assertThat(response.getName()).isEqualTo("Giáo án Tăng cơ 3 buổi/tuần");
        assertThat(response.getTrainerId()).isEqualTo(trainerId);
        assertThat(response.getClientId()).isEqualTo(clientId);
        assertThat(response.getStatus()).isEqualTo(WorkoutPlanStatus.ACTIVE);
        assertThat(response.getTrainingDays()).hasSize(3);

        ArgumentCaptor<WorkoutPlanEntity> captor = ArgumentCaptor.forClass(WorkoutPlanEntity.class);
        verify(workoutPlanRepository, times(1)).save(captor.capture());
        WorkoutPlanEntity saved = captor.getValue();
        assertThat(saved.getTrainingDays().get(0).getExercises()).hasSize(1);
        assertThat(saved.getTrainingDays().get(0).getExercises().get(0).getId()).isNotBlank();
    }

    @Test
    @DisplayName("Ném lỗi khi Client không thuộc quyền quản lý của PT")
    void createPlan_ClientNotAssignedToTrainer() {
        String trainerId = "pt-123";
        String clientId = "client-456";

        ClientEntity client = ClientEntity.builder()
                .id(clientId)
                .fullName("Nguyễn Văn A")
                .assignedTrainerId("other-pt-999")
                .build();

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        CreateWorkoutPlanRequest request = CreateWorkoutPlanRequest.builder()
                .clientId(clientId)
                .name("Giáo án tập")
                .startDate(LocalDate.of(2026, 12, 1))
                .endDate(LocalDate.of(2026, 12, 31))
                .build();

        assertThatThrownBy(() -> useCase.createPlan(trainerId, request))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> {
                    BusinessException be = (BusinessException) ex;
                    assertThat(be.getError()).isEqualTo(WorkoutPlanError.CLIENT_NOT_ASSIGNED_TO_TRAINER);
                });

        verify(workoutPlanRepository, never()).save(any());
    }

    @Test
    @DisplayName("Ném lỗi khi ngày bắt đầu lớn hơn ngày kết thúc")
    void createPlan_InvalidDateRange() {
        String trainerId = "pt-123";
        String clientId = "client-456";

        CreateWorkoutPlanRequest request = CreateWorkoutPlanRequest.builder()
                .clientId(clientId)
                .name("Giáo án")
                .startDate(LocalDate.of(2026, 12, 31))
                .endDate(LocalDate.of(2026, 12, 1))
                .build();

        assertThatThrownBy(() -> useCase.createPlan(trainerId, request))
                .isInstanceOf(BusinessException.class)
                .satisfies(ex -> {
                    BusinessException be = (BusinessException) ex;
                    assertThat(be.getError()).isEqualTo(WorkoutPlanError.INVALID_DATE_RANGE);
                });
    }
}

