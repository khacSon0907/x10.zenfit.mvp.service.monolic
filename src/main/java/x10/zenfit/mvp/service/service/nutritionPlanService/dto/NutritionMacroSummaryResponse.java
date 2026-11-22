package x10.zenfit.mvp.service.service.nutritionPlanService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NutritionMacroSummaryResponse {

    // Macro mục tiêu
    private Double targetCalories;
    private Double targetProtein;
    private Double targetCarbs;
    private Double targetFat;

    // Macro thực tế từ các món ăn
    private Double totalCalories;
    private Double totalProtein;
    private Double totalCarbs;
    private Double totalFat;

    // Phần trăm hoàn thành mục tiêu (%)
    private Double caloriesPercentage;
    private Double proteinPercentage;
    private Double carbsPercentage;
    private Double fatPercentage;
}

