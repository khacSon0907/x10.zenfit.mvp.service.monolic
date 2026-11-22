package x10.zenfit.mvp.service.infrastructure.nutrition.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.MealType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NutritionFoodDoc {
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
}

