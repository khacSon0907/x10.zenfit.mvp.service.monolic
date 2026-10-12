package x10.zenfit.mvp.service.service.orderService.getOrderUc;

import org.junit.jupiter.api.Test;
import x10.zenfit.mvp.service.Repository.IOrderRepository;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;
import x10.zenfit.mvp.service.service.orderService.OrderResponse;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GetOrderUcImplTest {

    @Test
    void lookupByKeywordReturnsSingleOrderWhenKeywordIsOrderCode() {
        IOrderRepository orderRepository = mock(IOrderRepository.class);
        GetOrderUcImpl useCase = new GetOrderUcImpl(orderRepository);
        OrderEntity order = OrderEntity.builder()
                .id("order-1")
                .orderCode("ORD20260723042110BB39EA")
                .receiverPhone("0901234567")
                .build();

        when(orderRepository.findByOrderCode("ORD20260723042110BB39EA")).thenReturn(Optional.of(order));

        List<OrderResponse> responses = useCase.executeLookupByKeyword("ORD20260723042110BB39EA");

        assertThat(responses).hasSize(1);
        assertThat(responses.getFirst().getOrderCode()).isEqualTo("ORD20260723042110BB39EA");
    }

    @Test
    void lookupByKeywordReturnsOrdersWhenKeywordIsReceiverPhone() {
        IOrderRepository orderRepository = mock(IOrderRepository.class);
        GetOrderUcImpl useCase = new GetOrderUcImpl(orderRepository);
        OrderEntity firstOrder = OrderEntity.builder()
                .id("order-1")
                .orderCode("ORD20260723042110BB39EA")
                .receiverPhone("0901234567")
                .build();
        OrderEntity secondOrder = OrderEntity.builder()
                .id("order-2")
                .orderCode("ORD20260724091500AA11CC")
                .receiverPhone("0901234567")
                .build();

        when(orderRepository.findByReceiverPhone("0901234567")).thenReturn(List.of(firstOrder, secondOrder));

        List<OrderResponse> responses = useCase.executeLookupByKeyword("090 123 4567");

        assertThat(responses).extracting(OrderResponse::getOrderCode)
                .containsExactly("ORD20260723042110BB39EA", "ORD20260724091500AA11CC");
    }
}
