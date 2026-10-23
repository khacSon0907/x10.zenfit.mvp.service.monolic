package x10.zenfit.mvp.service.common.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import x10.zenfit.mvp.service.common.domain.enums.ImageTargetType;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ImageSignatureRequest {

    private ImageTargetType targetType;
}
