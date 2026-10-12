package x10.zenfit.mvp.service.common.domain.dto.respone;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ImageSignatureResponse {
    private String cloudName;
    private String apiKey;
    private Long timestamp;
    private String signature;
    private String folder;
}
