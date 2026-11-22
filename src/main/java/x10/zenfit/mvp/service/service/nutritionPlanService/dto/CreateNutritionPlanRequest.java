package x10.zenfit.mvp.service.service.nutritionPlanService.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.NutritionPlanStatus;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateNutritionPlanRequest {

    @NotBlank(message = "Mã khách hàng (clientId) không được để trống")
    private String clientId;

    @NotBlank(message = "Tên kế hoạch không được để trống")
    private String name;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private NutritionPlanStatus status;

    @Valid
    private DailyMacroTargetRequest dailyMacroTarget;

    @Valid
    private List<NutritionFoodRequest> foods;
}

