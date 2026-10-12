package x10.zenfit.mvp.service.service.userService.updateUserUc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserRequest {
    private String username;
    private String phoneNumber;
    private String avatarUrl;
    private LocalDate birthday;
}
