package x10.zenfit.mvp.service.service.nutritionPlanService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.entities.nutrition.NutritionPlanEntity;
import x10.zenfit.mvp.service.common.domain.enums.NutritionPlanStatus;
import x10.zenfit.mvp.service.service.nutritionPlanService.util.MacroCalculator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NutritionPlanResponse {

    private String id;
    private String trainerId;
    private String clientId;
    private String clientName;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private NutritionPlanStatus status;
    private DailyMacroTargetResponse dailyMacroTarget;

    @Builder.Default
    private List<NutritionFoodResponse> foods = new ArrayList<>();

    private NutritionMacroSummaryResponse macroSummary;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static NutritionPlanResponse fromEntity(NutritionPlanEntity entity, String clientName) {
        if (entity == null) {
            return null;
        }

        DailyMacroTargetResponse targetResponse = null;
        if (entity.getDailyMacroTarget() != null) {
            targetResponse = DailyMacroTargetResponse.builder()
                    .calories(entity.getDailyMacroTarget().getCalories())
                    .protein(entity.getDailyMacroTarget().getProtein())
                    .carbs(entity.getDailyMacroTarget().getCarbs())
                    .fat(entity.getDailyMacroTarget().getFat())
                    .build();
        }

        List<NutritionFoodResponse> foodResponses = new ArrayList<>();
        if (entity.getFoods() != null) {
            foodResponses = entity.getFoods().stream()
                    .map(NutritionFoodResponse::fromEntity)
                    .toList();
        }

        NutritionMacroSummaryResponse summary = MacroCalculator.calculateSummary(
                entity.getDailyMacroTarget(),
                entity.getFoods()
        );

        return NutritionPlanResponse.builder()
                .id(entity.getId())
                .trainerId(entity.getTrainerId())
                .clientId(entity.getClientId())
                .clientName(clientName)
                .name(entity.getName())
                .description(entity.getDescription())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .status(entity.getStatus())
                .dailyMacroTarget(targetResponse)
                .foods(foodResponses)
                .macroSummary(summary)
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
}

