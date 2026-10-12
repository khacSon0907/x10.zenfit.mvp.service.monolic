package x10.zenfit.mvp.service.service.productService.getAllProductsUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import x10.zenfit.mvp.service.Repository.IProductRepository;
import x10.zenfit.mvp.service.common.domain.entities.product.ProductEntity;
import x10.zenfit.mvp.service.share.exception.BusinessException;
import x10.zenfit.mvp.service.share.exception.product.ProductError;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetAllProductsUcImpl implements IGetAllProductsUc {

    private static final int DEFAULT_LIMIT = 20;
    private static final int MAX_LIMIT = 100;
    private static final String CURSOR_SEPARATOR = "|";

    private final IProductRepository productRepository;

    @Override
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public ProductCursorPageResponse getAllProductsByCursor(String cursor, Integer limit) {
        int pageSize = normalizeLimit(limit);
        CursorValue cursorValue = decodeCursor(cursor);

        List<ProductEntity> products = productRepository.findAllByCursor(
                cursorValue.createdAt(),
                cursorValue.id(),
                pageSize + 1
        );

        boolean hasNext = products.size() > pageSize;
        List<ProductEntity> items = hasNext ? products.subList(0, pageSize) : products;
        ProductEntity lastItem = items.isEmpty() ? null : items.get(items.size() - 1);

        return ProductCursorPageResponse.builder()
                .items(items)
                .nextCursor(hasNext ? encodeCursor(lastItem) : null)
                .hasNext(hasNext)
                .limit(pageSize)
                .build();
    }

    private int normalizeLimit(Integer limit) {
        if (limit == null) {
            return DEFAULT_LIMIT;
        }

        if (limit < 1) {
            return DEFAULT_LIMIT;
        }

        return Math.min(limit, MAX_LIMIT);
    }

    private CursorValue decodeCursor(String cursor) {
        if (cursor == null || cursor.isBlank()) {
            return new CursorValue(null, null);
        }

        try {
            String decoded = new String(
                    Base64.getUrlDecoder().decode(cursor),
                    StandardCharsets.UTF_8
            );
            String[] parts = decoded.split("\\" + CURSOR_SEPARATOR, 2);
            if (parts.length != 2 || parts[0].isBlank() || parts[1].isBlank()) {
                throw new IllegalArgumentException("Invalid cursor");
            }

            return new CursorValue(LocalDateTime.parse(parts[0]), parts[1]);
        } catch (Exception e) {
            throw new BusinessException(ProductError.INVALID_PRODUCT_DATA, "Cursor phan trang khong hop le");
        }
    }

    private String encodeCursor(ProductEntity product) {
        String rawCursor = product.getCreatedAt() + CURSOR_SEPARATOR + product.getId();
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(rawCursor.getBytes(StandardCharsets.UTF_8));
    }

    private record CursorValue(LocalDateTime createdAt, String id) {
    }
}
