package x10.zenfit.mvp.service.service.nutritionPlanService.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.DailyMacroTarget;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionFoodEntity;
import x10.zenfit.mvp.service.common.domain.enums.MealType;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.NutritionMacroSummaryResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MacroCalculatorTest {

    @Test
    @DisplayName("Tính toán tổng macro và phần trăm hoàn thành chính xác khi có target và danh sách món ăn")
    void calculateSummary_Success() {
        DailyMacroTarget target = DailyMacroTarget.builder()
                .calories(2000.0)
                .protein(150.0)
                .carbs(200.0)
                .fat(60.0)
                .build();

        List<NutritionFoodEntity> foods = List.of(
                NutritionFoodEntity.builder()
                        .id("1")
                        .mealType(MealType.BREAKFAST)
                        .foodName("Yến mạch chuối")
                        .calories(400.0)
                        .protein(15.0)
                        .carbs(70.0)
                        .fat(6.0)
                        .build(),
                NutritionFoodEntity.builder()
                        .id("2")
                        .mealType(MealType.LUNCH)
                        .foodName("Ức gà áp chảo và cơm gạo lứt")
                        .calories(600.0)
                        .protein(60.0)
                        .carbs(60.0)
                        .fat(12.0)
                        .build()
        );

        NutritionMacroSummaryResponse summary = MacroCalculator.calculateSummary(target, foods);

        assertThat(summary.getTargetCalories()).isEqualTo(2000.0);
        assertThat(summary.getTargetProtein()).isEqualTo(150.0);
        assertThat(summary.getTargetCarbs()).isEqualTo(200.0);
        assertThat(summary.getTargetFat()).isEqualTo(60.0);

        // Totals: 400 + 600 = 1000 cal, 15 + 60 = 75 protein, 70 + 60 = 130 carbs, 6 + 12 = 18 fat
        assertThat(summary.getTotalCalories()).isEqualTo(1000.0);
        assertThat(summary.getTotalProtein()).isEqualTo(75.0);
        assertThat(summary.getTotalCarbs()).isEqualTo(130.0);
        assertThat(summary.getTotalFat()).isEqualTo(18.0);

        // Percentages: 1000 / 2000 = 50%, 75 / 150 = 50%, 130 / 200 = 65%, 18 / 60 = 30%
        assertThat(summary.getCaloriesPercentage()).isEqualTo(50.0);
        assertThat(summary.getProteinPercentage()).isEqualTo(50.0);
        assertThat(summary.getCarbsPercentage()).isEqualTo(65.0);
        assertThat(summary.getFatPercentage()).isEqualTo(30.0);
    }

    @Test
    @DisplayName("Xử lý an toàn khi target bằng 0 để tránh lỗi chia cho 0")
    void calculateSummary_ZeroTarget_ShouldPreventDivisionByZero() {
        DailyMacroTarget target = DailyMacroTarget.builder()
                .calories(0.0)
                .protein(0.0)
                .carbs(0.0)
                .fat(0.0)
                .build();

        List<NutritionFoodEntity> foods = List.of(
                NutritionFoodEntity.builder()
                        .id("1")
                        .calories(350.0)
                        .protein(30.0)
                        .carbs(20.0)
                        .fat(10.0)
                        .build()
        );

        NutritionMacroSummaryResponse summary = MacroCalculator.calculateSummary(target, foods);

        assertThat(summary.getTotalCalories()).isEqualTo(350.0);
        assertThat(summary.getCaloriesPercentage()).isEqualTo(0.0);
        assertThat(summary.getProteinPercentage()).isEqualTo(0.0);
        assertThat(summary.getCarbsPercentage()).isEqualTo(0.0);
        assertThat(summary.getFatPercentage()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("Xử lý an toàn khi danh sách món ăn rỗng hoặc target null")
    void calculateSummary_EmptyFoodsOrNullTarget() {
        NutritionMacroSummaryResponse summary = MacroCalculator.calculateSummary(null, null);

        assertThat(summary.getTargetCalories()).isEqualTo(0.0);
        assertThat(summary.getTotalCalories()).isEqualTo(0.0);
        assertThat(summary.getCaloriesPercentage()).isEqualTo(0.0);
    }
}

