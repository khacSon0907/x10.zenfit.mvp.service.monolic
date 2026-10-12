package x10.zenfit.mvp.service.Repository;

import x10.zenfit.mvp.service.common.domain.entities.cart.CartEntity;

import java.util.Optional;

public interface ICartRepository {

    Optional<CartEntity> findByUserId(String userId);

    CartEntity save(CartEntity cart);

    void deleteByUserId(String userId);

}

