package x10.zenfit.mvp.service.Repository;

import x10.zenfit.mvp.service.common.domain.entities.favorite.FavoriteEntity;

import java.util.Optional;

public interface IFavoriteRepository {

    Optional<FavoriteEntity> findByUserId(String userId);

    FavoriteEntity save(FavoriteEntity favorite);

    void deleteByUserId(String userId);
}

