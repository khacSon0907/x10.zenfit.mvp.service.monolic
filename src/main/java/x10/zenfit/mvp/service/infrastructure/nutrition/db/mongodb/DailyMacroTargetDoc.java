package x10.zenfit.mvp.service.infrastructure.nutrition.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyMacroTargetDoc {
    private Double calories;
    private Double protein;
    private Double carbs;
    private Double fat;
}

