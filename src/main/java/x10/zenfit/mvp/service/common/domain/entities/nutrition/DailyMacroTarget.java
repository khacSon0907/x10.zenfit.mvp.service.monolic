package x10.zenfit.mvp.service.common.domain.entities.nutrition;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailyMacroTarget {
    private Double calories; // kcal
    private Double protein;  // gram
    private Double carbs;    // gram
    private Double fat;      // gram
}

