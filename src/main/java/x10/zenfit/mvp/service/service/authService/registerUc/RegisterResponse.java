package x10.zenfit.mvp.service.service.authService.registerUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterResponse {

    private String id;
    private String username;
    private String email;
    private boolean requiresOtp;
}