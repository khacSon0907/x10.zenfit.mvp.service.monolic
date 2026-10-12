package x10.zenfit.mvp.service.infrastructure.cart.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.Repository.ICartRepository;
import x10.zenfit.mvp.service.common.domain.entities.cart.CartEntity;
import x10.zenfit.mvp.service.infrastructure.cart.db.mongodb.CartDocument;
import x10.zenfit.mvp.service.infrastructure.cart.db.mongodb.CartMongoRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CartRepositoryImpl implements ICartRepository {

    private final CartMongoRepository cartMongoRepository;

    @Override
    public Optional<CartEntity> findByUserId(String userId) {
        return cartMongoRepository.findByUserId(userId)
                .map(CartMapper::toEntity);
    }

    @Override
    public CartEntity save(CartEntity cart) {
        CartDocument doc = CartMapper.toDocument(cart);
        CartDocument saved = cartMongoRepository.save(doc);
        return CartMapper.toEntity(saved);
    }

    @Override
    public void deleteByUserId(String userId) {
        cartMongoRepository.deleteByUserId(userId);
    }
}

