package x10.zenfit.mvp.service.service.authService.oauth2LoginUc;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OAuth2ExchangeRequest {

    @NotBlank(message = "OAuth2 login code is required")
    private String code;
}
