package x10.zenfit.mvp.service.service.authService.refreshTokenUc;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenReq {

    @NotBlank(message = "Refresh token không được để trống")
    private String refreshToken;
}
