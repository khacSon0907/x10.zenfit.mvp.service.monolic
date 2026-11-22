package x10.zenfit.mvp.service.service.nutritionPlanService.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateMacroTargetRequest {

    @NotNull(message = "Calories mục tiêu là bắt buộc")
    @PositiveOrZero(message = "Calories mục tiêu không được âm")
    private Double calories;

    @NotNull(message = "Protein mục tiêu là bắt buộc")
    @PositiveOrZero(message = "Protein mục tiêu không được âm")
    private Double protein;

    @NotNull(message = "Carbs mục tiêu là bắt buộc")
    @PositiveOrZero(message = "Carbs mục tiêu không được âm")
    private Double carbs;

    @NotNull(message = "Fat mục tiêu là bắt buộc")
    @PositiveOrZero(message = "Fat mục tiêu không được âm")
    private Double fat;
}

