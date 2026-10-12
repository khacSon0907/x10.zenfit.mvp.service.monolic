package x10.zenfit.mvp.service.service.orderService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.CancelReasonType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelReasonOptionResponse {
    private CancelReasonType type;
    private String label;
    private boolean requireDetail;
}
