package x10.zenfit.mvp.service.service.nutritionPlanService.createPlanUc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import x10.zenfit.mvp.service.Repository.IClientRepository;
import x10.zenfit.mvp.service.Repository.INutritionPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.client.ClientEntity;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionPlanEntity;
import x10.zenfit.mvp.service.common.domain.enums.NutritionPlanStatus;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.CreateNutritionPlanRequest;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.DailyMacroTargetRequest;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.NutritionPlanResponse;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.nutrition.NutritionPlanError;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateNutritionPlanUcImplTest {

    private INutritionPlanRepository nutritionPlanRepository;
    private IClientRepository clientRepository;
    private CreateNutritionPlanUcImpl useCase;

    @BeforeEach
    void setUp() {
        nutritionPlanRepository = mock(INutritionPlanRepository.class);
        clientRepository = mock(IClientRepository.class);
        useCase = new CreateNutritionPlanUcImpl(nutritionPlanRepository, clientRepository);
    }

    @Test
    @DisplayName("Tạo kế hoạch dinh dưỡng thành công khi Client thuộc quyền quản lý của PT")
    void createPlan_Success() {
        String trainerId = "pt-123";
        String clientId = "client-456";

        ClientEntity client = ClientEntity.builder()
                .id(clientId)
                .fullName("Nguyễn Văn A")
                .assignedTrainerId(trainerId)
                .build();

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(nutritionPlanRepository.save(any(NutritionPlanEntity.class))).thenAnswer(invocation -> {
            NutritionPlanEntity entity = invocation.getArgument(0);
            entity.setId("plan-999");
            return entity;
        });

        CreateNutritionPlanRequest request = CreateNutritionPlanRequest.builder()
                .clientId(clientId)
                .name("Kế hoạch siết cân tháng 12")
                .description("Ăn thâm hụt calo nhẹ")
                .startDate(LocalDate.of(2026, 12, 1))
                .endDate(LocalDate.of(2026, 12, 31))
                .status(NutritionPlanStatus.ACTIVE)
                .dailyMacroTarget(DailyMacroTargetRequest.builder()
                        .calories(2200.0)
                        .protein(160.0)
                        .carbs(230.0)
                        .fat(65.0)
                        .build())
                .build();

        NutritionPlanResponse response = useCase.createPlan(trainerId, request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo("plan-999");
        assertThat(response.getTrainerId()).isEqualTo(trainerId);
        assertThat(response.getClientId()).isEqualTo(clientId);
        assertThat(response.getClientName()).isEqualTo("Nguyễn Văn A");
        assertThat(response.getName()).isEqualTo("Kế hoạch siết cân tháng 12");
        assertThat(response.getDailyMacroTarget().getCalories()).isEqualTo(2200.0);

        ArgumentCaptor<NutritionPlanEntity> captor = ArgumentCaptor.forClass(NutritionPlanEntity.class);
        verify(nutritionPlanRepository).save(captor.capture());
        assertThat(captor.getValue().getTrainerId()).isEqualTo(trainerId);
    }

    @Test
    @DisplayName("Báo lỗi khi Client không tồn tại")
    void createPlan_ClientNotFound_ShouldThrowException() {
        String trainerId = "pt-123";
        String clientId = "non-existing";

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        CreateNutritionPlanRequest request = CreateNutritionPlanRequest.builder()
                .clientId(clientId)
                .name("Kế hoạch mẫu")
                .build();

        assertThatThrownBy(() -> useCase.createPlan(trainerId, request))
                .isInstanceOf(BusinessException.class)
                .matches(e -> ((BusinessException) e).getError() == NutritionPlanError.CLIENT_NOT_FOUND);
    }

    @Test
    @DisplayName("Báo lỗi khi Client không thuộc quyền quản lý của PT (assignedTrainerId khác)")
    void createPlan_ClientNotAssignedToTrainer_ShouldThrowException() {
        String trainerId = "pt-123";
        String clientId = "client-other";

        ClientEntity client = ClientEntity.builder()
                .id(clientId)
                .fullName("Khách hàng của PT khác")
                .assignedTrainerId("pt-other") // Khác với trainerId đang gọi
                .build();

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        CreateNutritionPlanRequest request = CreateNutritionPlanRequest.builder()
                .clientId(clientId)
                .name("Kế hoạch mẫu")
                .build();

        assertThatThrownBy(() -> useCase.createPlan(trainerId, request))
                .isInstanceOf(BusinessException.class)
                .matches(e -> ((BusinessException) e).getError() == NutritionPlanError.CLIENT_NOT_ASSIGNED_TO_TRAINER);
    }

    @Test
    @DisplayName("Báo lỗi khi startDate lớn hơn endDate")
    void createPlan_InvalidDateRange_ShouldThrowException() {
        String trainerId = "pt-123";
        CreateNutritionPlanRequest request = CreateNutritionPlanRequest.builder()
                .clientId("client-1")
                .name("Kế hoạch lỗi ngày")
                .startDate(LocalDate.of(2026, 12, 31))
                .endDate(LocalDate.of(2026, 12, 1)) // Sau trước trước
                .build();

        assertThatThrownBy(() -> useCase.createPlan(trainerId, request))
                .isInstanceOf(BusinessException.class)
                .matches(e -> ((BusinessException) e).getError() == NutritionPlanError.INVALID_DATE_RANGE);
    }
}

