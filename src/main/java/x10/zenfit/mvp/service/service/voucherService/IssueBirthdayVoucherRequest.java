package x10.zenfit.mvp.service.service.voucherService;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class IssueBirthdayVoucherRequest {
    private LocalDateTime startAt;
    private LocalDateTime endAt;
}
