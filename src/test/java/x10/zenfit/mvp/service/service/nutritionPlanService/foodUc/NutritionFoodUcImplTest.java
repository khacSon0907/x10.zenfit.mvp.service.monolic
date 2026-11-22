package x10.zenfit.mvp.service.service.nutritionPlanService.foodUc;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import x10.zenfit.mvp.service.Repository.INutritionPlanRepository;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionFoodEntity;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionPlanEntity;
import x10.zenfit.mvp.service.common.domain.enums.MealType;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.NutritionFoodRequest;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.NutritionFoodResponse;
import x10.zenfit.mvp.service.service.nutritionPlanService.dto.UpdateNutritionFoodRequest;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.nutrition.NutritionPlanError;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class NutritionFoodUcImplTest {

    private INutritionPlanRepository nutritionPlanRepository;
    private AddNutritionFoodUcImpl addFoodUc;
    private UpdateNutritionFoodUcImpl updateFoodUc;
    private DeleteNutritionFoodUcImpl deleteFoodUc;

    @BeforeEach
    void setUp() {
        nutritionPlanRepository = mock(INutritionPlanRepository.class);
        addFoodUc = new AddNutritionFoodUcImpl(nutritionPlanRepository);
        updateFoodUc = new UpdateNutritionFoodUcImpl(nutritionPlanRepository);
        deleteFoodUc = new DeleteNutritionFoodUcImpl(nutritionPlanRepository);
    }

    @Test
    @DisplayName("Thêm món ăn vào kế hoạch thành công")
    void addFood_Success() {
        String trainerId = "pt-1";
        String planId = "plan-1";

        NutritionPlanEntity plan = NutritionPlanEntity.builder()
                .id(planId)
                .trainerId(trainerId)
                .foods(new ArrayList<>())
                .build();

        when(nutritionPlanRepository.findById(planId)).thenReturn(Optional.of(plan));

        NutritionFoodRequest request = NutritionFoodRequest.builder()
                .mealType(MealType.BREAKFAST)
                .foodName("Trứng ốp la 2 quả")
                .quantity(2.0)
                .unit("quả")
                .calories(150.0)
                .protein(12.0)
                .carbs(1.0)
                .fat(10.0)
                .build();

        NutritionFoodResponse response = addFoodUc.addFood(trainerId, planId, request);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isNotBlank();
        assertThat(response.getFoodName()).isEqualTo("Trứng ốp la 2 quả");
        assertThat(response.getCalories()).isEqualTo(150.0);
        assertThat(plan.getFoods()).hasSize(1);
        verify(nutritionPlanRepository).save(plan);
    }

    @Test
    @DisplayName("Báo lỗi ACCESS_DENIED khi PT khác cố gắng sửa món ăn")
    void updateFood_AccessDenied_WhenOtherTrainer() {
        String trainerId = "pt-hacker";
        String planId = "plan-1";
        String foodId = "food-1";

        NutritionPlanEntity plan = NutritionPlanEntity.builder()
                .id(planId)
                .trainerId("pt-legit") // Chủ sở hữu là PT khác
                .build();

        when(nutritionPlanRepository.findById(planId)).thenReturn(Optional.of(plan));

        UpdateNutritionFoodRequest request = UpdateNutritionFoodRequest.builder()
                .foodName("Tên sửa trộm")
                .build();

        assertThatThrownBy(() -> updateFoodUc.updateFood(trainerId, planId, foodId, request))
                .isInstanceOf(BusinessException.class)
                .matches(e -> ((BusinessException) e).getError() == NutritionPlanError.ACCESS_DENIED);
    }

    @Test
    @DisplayName("Xóa món ăn thành công khỏi kế hoạch")
    void deleteFood_Success() {
        String trainerId = "pt-1";
        String planId = "plan-1";
        String foodId = "food-to-delete";

        NutritionFoodEntity food = NutritionFoodEntity.builder()
                .id(foodId)
                .foodName("Thịt bò xào cần tây")
                .build();

        NutritionPlanEntity plan = NutritionPlanEntity.builder()
                .id(planId)
                .trainerId(trainerId)
                .foods(new ArrayList<>(List.of(food)))
                .build();

        when(nutritionPlanRepository.findById(planId)).thenReturn(Optional.of(plan));

        deleteFoodUc.deleteFood(trainerId, planId, foodId);

        assertThat(plan.getFoods()).isEmpty();
        verify(nutritionPlanRepository).save(plan);
    }

    @Test
    @DisplayName("Báo lỗi FOOD_NOT_FOUND khi xóa món ăn không tồn tại trong kế hoạch")
    void deleteFood_FoodNotFound() {
        String trainerId = "pt-1";
        String planId = "plan-1";

        NutritionPlanEntity plan = NutritionPlanEntity.builder()
                .id(planId)
                .trainerId(trainerId)
                .foods(new ArrayList<>())
                .build();

        when(nutritionPlanRepository.findById(planId)).thenReturn(Optional.of(plan));

        assertThatThrownBy(() -> deleteFoodUc.deleteFood(trainerId, planId, "non-existing-food"))
                .isInstanceOf(BusinessException.class)
                .matches(e -> ((BusinessException) e).getError() == NutritionPlanError.FOOD_NOT_FOUND);
    }
}

