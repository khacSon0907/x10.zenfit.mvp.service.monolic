package x10.zenfit.mvp.service.service.nutritionPlanService.util;

import x10.zenfit.mvp.service.common.domain.entities.nutrition.DailyMacroTarget;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionFoodEntity;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.NutritionMacroSummaryResponse;

import java.util.List;

public class MacroCalculator {

    public static NutritionMacroSummaryResponse calculateSummary(
            DailyMacroTarget target,
            List<NutritionFoodEntity> foods
    ) {
        double targetCalories = (target != null && target.getCalories() != null) ? target.getCalories() : 0.0;
        double targetProtein = (target != null && target.getProtein() != null) ? target.getProtein() : 0.0;
        double targetCarbs = (target != null && target.getCarbs() != null) ? target.getCarbs() : 0.0;
        double targetFat = (target != null && target.getFat() != null) ? target.getFat() : 0.0;

        double totalCalories = 0.0;
        double totalProtein = 0.0;
        double totalCarbs = 0.0;
        double totalFat = 0.0;

        if (foods != null) {
            for (NutritionFoodEntity food : foods) {
                if (food.getCalories() != null) {
                    totalCalories += food.getCalories();
                }
                if (food.getProtein() != null) {
                    totalProtein += food.getProtein();
                }
                if (food.getCarbs() != null) {
                    totalCarbs += food.getCarbs();
                }
                if (food.getFat() != null) {
                    totalFat += food.getFat();
                }
            }
        }

        double caloriesPercentage = targetCalories > 0.0 ? round((totalCalories / targetCalories) * 100.0) : 0.0;
        double proteinPercentage = targetProtein > 0.0 ? round((totalProtein / targetProtein) * 100.0) : 0.0;
        double carbsPercentage = targetCarbs > 0.0 ? round((totalCarbs / targetCarbs) * 100.0) : 0.0;
        double fatPercentage = targetFat > 0.0 ? round((totalFat / targetFat) * 100.0) : 0.0;

        return NutritionMacroSummaryResponse.builder()
                .targetCalories(round(targetCalories))
                .targetProtein(round(targetProtein))
                .targetCarbs(round(targetCarbs))
                .targetFat(round(targetFat))
                .totalCalories(round(totalCalories))
                .totalProtein(round(totalProtein))
                .totalCarbs(round(totalCarbs))
                .totalFat(round(totalFat))
                .caloriesPercentage(caloriesPercentage)
                .proteinPercentage(proteinPercentage)
                .carbsPercentage(carbsPercentage)
                .fatPercentage(fatPercentage)
                .build();
    }

    private static double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}

