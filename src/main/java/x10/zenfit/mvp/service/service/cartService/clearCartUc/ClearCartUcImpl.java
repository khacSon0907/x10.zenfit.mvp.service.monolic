package x10.zenfit.mvp.service.service.cartService.clearCartUc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.Repository.ICartRepository;

@Service
@RequiredArgsConstructor
public class ClearCartUcImpl implements IClearCartUc {

    private final ICartRepository cartRepository;

    @Override
    public String execute(String userId) {
        cartRepository.deleteByUserId(userId);
        return "Cart cleared successfully";
    }
}

