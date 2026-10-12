package x10.zenfit.mvp.service.infrastructure.cart.db.mongodb;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "carts")
public class CartDocument {
    @Id
    private String id;

    private String userId;

    private List<CartItemDocument> items;

    private Integer totalQuantity;

    private BigDecimal totalAmount;

    private Instant createdAt;

    private Instant updatedAt;
}

