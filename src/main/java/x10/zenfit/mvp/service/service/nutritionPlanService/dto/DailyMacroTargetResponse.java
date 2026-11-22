package x10.zenfit.mvp.service.service.nutritionPlanService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyMacroTargetResponse {
    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fat;
}

