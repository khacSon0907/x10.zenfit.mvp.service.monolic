package x10.zenfit.mvp.service.service.nutritionPlanService.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.MealType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NutritionFoodRequest {

    @NotNull(message = "Bữa ăn (mealType) không được để trống")
    private MealType mealType;

    @NotBlank(message = "Tên món ăn không được để trống")
    private String foodName;

    @NotNull(message = "Số lượng không được để trống")
    @Positive(message = "Số lượng phải lớn hơn 0")
    private Double quantity;

    private String unit;

    @PositiveOrZero(message = "Calories không được âm")
    private Double calories;

    @PositiveOrZero(message = "Protein không được âm")
    private Double protein;

    @PositiveOrZero(message = "Carbs không được âm")
    private Double carbs;

    @PositiveOrZero(message = "Fat không được âm")
    private Double fat;

    private String note;
}

