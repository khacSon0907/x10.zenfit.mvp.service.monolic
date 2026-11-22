package x10.zenfit.mvp.service.service.nutritionPlanService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionFoodEntity;
import x10.zenfit.mvp.service.common.domain.enums.MealType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NutritionFoodResponse {

    private String id;
    private MealType mealType;
    private String foodName;
    private Double quantity;
    private String unit;
    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fat;
    private String note;

    public static NutritionFoodResponse fromEntity(NutritionFoodEntity entity) {
        if (entity == null) {
            return null;
        }
        return NutritionFoodResponse.builder()
                .id(entity.getId())
                .mealType(entity.getMealType())
                .foodName(entity.getFoodName())
                .quantity(entity.getQuantity())
                .unit(entity.getUnit())
                .calories(entity.getCalories())
                .protein(entity.getProtein())
                .carbs(entity.getCarbs())
                .fat(entity.getFat())
                .note(entity.getNote())
                .build();
    }
}

