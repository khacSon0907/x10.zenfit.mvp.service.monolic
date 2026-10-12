package x10.zenfit.mvp.service.service.authService.oauth2LoginUc;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleUserInfo {
    private String email;
    private String name;
    private String picture;
    private String googleId;
    private boolean emailVerified;
}