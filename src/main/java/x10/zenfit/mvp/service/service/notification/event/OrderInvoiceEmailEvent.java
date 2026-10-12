package x10.zenfit.mvp.service.service.notification.event;

import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;

public record OrderInvoiceEmailEvent(
        String email,
        String username,
        OrderEntity order
) {
}
