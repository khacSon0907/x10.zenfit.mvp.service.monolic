package x10.zenfit.mvp.service.service.authService.loginUc;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String id;
    private String username;
    private String email;
    private List<String> roleIds;
    private List<String> roles;
    private String accessToken;

    @JsonIgnore
    private String refreshToken;
}
