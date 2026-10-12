package x10.zenfit.mvp.service.service.orderService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IProductRepository;
import x10.zenfit.mvp.service.common.domain.enums.CancelReasonType;
import x10.zenfit.mvp.service.common.domain.entities.product.ColorVariantEntity;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderEntity;
import x10.zenfit.mvp.service.common.domain.entities.order.OrderItem;
import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;
import x10.zenfit.mvp.service.common.domain.entities.product.SizeVariantEntity;
import x10.zenfit.mvp.service.common.domain.enums.OrderStatus;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.order.OrderError;
import x10.zenfit.mvp.service.share.exception.product.ProductError;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderInventoryService {

    private final IProductRepository productRepository;

    public void confirmOrder(OrderEntity order) {
        if (order.getStatus() == OrderStatus.CONFIRMED) {
            return;
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessException(
                    OrderError.INVALID_ORDER_STATUS,
                    "Chi co the xac nhan don hang dang cho xu ly"
            );
        }

        deductStock(order.getItems());
        order.setStatus(OrderStatus.CONFIRMED);
    }

    public void cancelOrder(
            OrderEntity order,
            CancelReasonType cancelReasonType,
            String cancelReason,
            String cancelReasonDetail
    ) {
        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new BusinessException(
                    OrderError.INVALID_ORDER_STATUS,
                    "Chi co the huy don hang dang cho xu ly hoac da xac nhan"
            );
        }

        if (order.getStatus() == OrderStatus.CONFIRMED) {
            restoreStock(order.getItems());
        }

        order.setStatus(OrderStatus.CANCELLED);
        order.setCancelReasonType(cancelReasonType);
        order.setCancelReason(cancelReason);
        order.setCancelReasonDetail(cancelReasonDetail);
    }

    private void deductStock(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA);
        }

        Map<String, ProductEntity> productsToSave = new LinkedHashMap<>();

        for (OrderItem item : items) {
            ProductEntity product = productsToSave.computeIfAbsent(
                    item.getProductId(),
                    productId -> productRepository.findById(productId)
                            .orElseThrow(() -> new BusinessException(ProductError.PRODUCT_NOT_FOUND))
            );

            SizeVariantEntity size = findSizeVariant(product, item);
            int currentQuantity = size.getQuantity() == null ? 0 : size.getQuantity();
            int orderedQuantity = item.getQuantity() == null ? 0 : item.getQuantity();

            if (orderedQuantity <= 0) {
                throw new BusinessException(OrderError.INVALID_ORDER_DATA);
            }

            if (currentQuantity < orderedQuantity) {
                throw new BusinessException(
                        OrderError.INSUFFICIENT_STOCK,
                        "San pham " + item.getProductName() + " khong du so luong ton kho"
                );
            }

            size.setQuantity(currentQuantity - orderedQuantity);
            size.setSoldQuantity(defaultZero(size.getSoldQuantity()) + orderedQuantity);
        }

        for (ProductEntity product : productsToSave.values()) {
            productRepository.save(product);
        }
    }

    private void restoreStock(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA);
        }

        Map<String, ProductEntity> productsToSave = new LinkedHashMap<>();

        for (OrderItem item : items) {
            ProductEntity product = productsToSave.computeIfAbsent(
                    item.getProductId(),
                    productId -> productRepository.findById(productId)
                            .orElseThrow(() -> new BusinessException(ProductError.PRODUCT_NOT_FOUND))
            );

            SizeVariantEntity size = findSizeVariant(product, item);
            int currentQuantity = size.getQuantity() == null ? 0 : size.getQuantity();
            int currentSoldQuantity = defaultZero(size.getSoldQuantity());
            int orderedQuantity = item.getQuantity() == null ? 0 : item.getQuantity();

            if (orderedQuantity <= 0) {
                throw new BusinessException(OrderError.INVALID_ORDER_DATA);
            }

            size.setQuantity(currentQuantity + orderedQuantity);
            size.setSoldQuantity(Math.max(0, currentSoldQuantity - orderedQuantity));
        }

        for (ProductEntity product : productsToSave.values()) {
            productRepository.save(product);
        }
    }

    private SizeVariantEntity findSizeVariant(ProductEntity product, OrderItem item) {
        if (product.getColors() == null || product.getColors().isEmpty()) {
            throw new BusinessException(OrderError.INVALID_ORDER_DATA, "San pham khong co bien the mau");
        }

        for (ColorVariantEntity color : product.getColors()) {
            if (!matches(item.getColorId(), color.getId())) {
                continue;
            }

            if (color.getSizes() == null || color.getSizes().isEmpty()) {
                throw new BusinessException(OrderError.INVALID_ORDER_DATA, "San pham khong co bien the size");
            }

            for (SizeVariantEntity size : color.getSizes()) {
                if (matches(item.getSizeId(), size.getId())) {
                    return size;
                }
            }
        }

        throw new BusinessException(
                OrderError.INVALID_ORDER_DATA,
                "Khong tim thay bien the san pham trong don hang"
        );
    }

    private boolean matches(String expected, String actual) {
        return expected != null && actual != null && expected.equals(actual);
    }

    private int defaultZero(Integer value) {
        return value == null ? 0 : value;
    }
}
