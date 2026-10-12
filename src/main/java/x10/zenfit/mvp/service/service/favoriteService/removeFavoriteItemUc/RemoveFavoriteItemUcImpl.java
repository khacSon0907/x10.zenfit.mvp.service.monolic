package x10.zenfit.mvp.service.service.favoriteService.removeFavoriteItemUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IFavoriteRepository;
import x10.zenfit.mvp.service.common.domain.entities.favorite.FavoriteEntity;
import x10.zenfit.mvp.service.common.domain.entities.favorite.FavoriteItem;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RemoveFavoriteItemUcImpl implements IRemoveFavoriteItemUc {

    private final IFavoriteRepository favoriteRepository;

    @Override
    public String execute(String userId, String productId) {
        Optional<FavoriteEntity> favOpt = favoriteRepository.findByUserId(userId);
        if (!favOpt.isPresent()) {
            return "FAVORITE.NOT_FOUND";
        }

        FavoriteEntity favorite = favOpt.get();
        List<FavoriteItem> items = favorite.getItems();
        if (items == null || items.isEmpty()) {
            return "FAVORITE.EMPTY";
        }

        List<FavoriteItem> remaining = items.stream()
                .filter(i -> !i.getProductId().equals(productId))
                .collect(Collectors.toList());

        favorite.setItems(remaining);
        favorite.setTotalItems(remaining.size());
        favorite.setUpdatedAt(Instant.now());
        favoriteRepository.save(favorite);

        return "FAVORITE.REMOVE_SUCCESS";
    }
}

