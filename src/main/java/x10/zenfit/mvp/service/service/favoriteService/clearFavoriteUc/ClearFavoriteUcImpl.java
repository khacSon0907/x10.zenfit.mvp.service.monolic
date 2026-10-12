package x10.zenfit.mvp.service.service.favoriteService.clearFavoriteUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.IFavoriteRepository;

@Service
@RequiredArgsConstructor
public class ClearFavoriteUcImpl implements IClearFavoriteUc {

    private final IFavoriteRepository favoriteRepository;

    @Override
    public String execute(String userId) {
        favoriteRepository.deleteByUserId(userId);
        return "FAVORITE.CLEAR_SUCCESS";
    }
}

