package x10.zenfit.mvp.service.service.orderService;

import org.junit.jupiter.api.Test;
import x10.zenfit.mvp.service.Repository.IProductRepository;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderItem;
import x10.zenfit.mvp.service.common.domain.entities.product.ColorVariantEntity;
import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;
import x10.zenfit.mvp.service.common.domain.entities.product.SizeVariantEntity;
import x10.zenfit.mvp.service.common.domain.enums.CancelReasonType;
import x10.zenfit.mvp.service.common.domain.enums.OrderStatus;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderInventoryServiceTest {

    @Test
    void cancelConfirmedOrderRestoresStockAndSoldQuantity() {
        IProductRepository productRepository = mock(IProductRepository.class);
        OrderInventoryService service = new OrderInventoryService(productRepository);

        SizeVariantEntity size = SizeVariantEntity.builder()
                .id("size-1")
                .quantity(3)
                .soldQuantity(7)
                .build();
        ProductEntity product = ProductEntity.builder()
                .id("product-1")
                .colors(List.of(ColorVariantEntity.builder()
                        .id("color-1")
                        .sizes(List.of(size))
                        .build()))
                .build();
        OrderEntity order = OrderEntity.builder()
                .status(OrderStatus.CONFIRMED)
                .items(List.of(OrderItem.builder()
                        .productId("product-1")
                        .productName("Ao thun")
                        .colorId("color-1")
                        .sizeId("size-1")
                        .quantity(2)
                        .build()))
                .build();

        when(productRepository.findById("product-1")).thenReturn(Optional.of(product));

        service.cancelOrder(
                order,
                CancelReasonType.OTHER,
                "Khach doi y",
                "Khach doi y"
        );

        assertThat(order.getStatus()).isEqualTo(OrderStatus.CANCELLED);
        assertThat(order.getCancelReasonType()).isEqualTo(CancelReasonType.OTHER);
        assertThat(order.getCancelReason()).isEqualTo("Khach doi y");
        assertThat(order.getCancelReasonDetail()).isEqualTo("Khach doi y");
        assertThat(size.getQuantity()).isEqualTo(5);
        assertThat(size.getSoldQuantity()).isEqualTo(5);
        verify(productRepository).save(product);
    }
}
