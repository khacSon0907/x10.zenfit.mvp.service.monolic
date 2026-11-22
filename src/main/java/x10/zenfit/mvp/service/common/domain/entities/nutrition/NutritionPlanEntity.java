package x10.zenfit.mvp.service.common.domain.entities.nutrition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.NutritionPlanStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NutritionPlanEntity {
    private String id;
    private String trainerId;
    private String clientId;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private NutritionPlanStatus status;
    private DailyMacroTarget dailyMacroTarget;

    @Builder.Default
    private List<NutritionFoodEntity> foods = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

