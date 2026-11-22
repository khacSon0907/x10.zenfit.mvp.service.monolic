package x10.zenfit.mvp.service.service.nutritionPlanService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.NutritionPlanStatus;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNutritionPlanRequest {

    private String name;

    private String description;

    private LocalDate startDate;

    private LocalDate endDate;

    private NutritionPlanStatus status;
}

