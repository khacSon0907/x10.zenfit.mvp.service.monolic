package x10.zenfit.mvp.service.infrastructure.favorite.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.Repository.IFavoriteRepository;
import x10.zenfit.mvp.service.common.domain.entities.favorite.FavoriteEntity;
import x10.zenfit.mvp.service.infrastructure.favorite.db.mongodb.FavoriteDocument;
import x10.zenfit.mvp.service.infrastructure.favorite.db.mongodb.FavoriteMongoRepository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class FavoriteRepositoryImpl implements IFavoriteRepository {

    private final FavoriteMongoRepository favoriteMongoRepository;

    @Override
    public Optional<FavoriteEntity> findByUserId(String userId) {
        return favoriteMongoRepository.findByUserId(userId)
                .map(FavoriteMapper::toEntity);
    }

    @Override
    public FavoriteEntity save(FavoriteEntity favorite) {
        FavoriteDocument doc = FavoriteMapper.toDocument(favorite);
        FavoriteDocument saved = favoriteMongoRepository.save(doc);
        return FavoriteMapper.toEntity(saved);
    }

    @Override
    public void deleteByUserId(String userId) {
        favoriteMongoRepository.deleteByUserId(userId);
    }
}
