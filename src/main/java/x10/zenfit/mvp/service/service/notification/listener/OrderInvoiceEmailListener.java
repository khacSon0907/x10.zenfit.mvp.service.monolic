package x10.zenfit.mvp.service.service.notification.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.service.notification.EmailPort;
import x10.zenfit.mvp.service.service.notification.event.OrderInvoiceEmailEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderInvoiceEmailListener {

    private final EmailPort emailPort;

    @Async("mailExecutor")
    @EventListener
    public void handleOrderInvoiceEmailEvent(OrderInvoiceEmailEvent event) {
        try {
            emailPort.sendOrderInvoiceEmail(
                    event.email(),
                    event.username(),
                    event.order()
            );
        } catch (Exception e) {
            log.error("Failed to send order invoice email to: {}", event.email(), e);
        }
    }
}
