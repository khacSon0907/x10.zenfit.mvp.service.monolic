package x10.zenfit.mvp.service.service.nutritionPlanService.dto;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyMacroTargetRequest {

    @PositiveOrZero(message = "Calories mục tiêu không được âm")
    private Double calories;

    @PositiveOrZero(message = "Protein mục tiêu không được âm")
    private Double protein;

    @PositiveOrZero(message = "Carbs mục tiêu không được âm")
    private Double carbs;

    @PositiveOrZero(message = "Fat mục tiêu không được âm")
    private Double fat;
}

