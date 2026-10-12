package x10.zenfit.mvp.service.service.cartService;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import x10.zenfit.mvp.service.service.cartService.addItemToCartUc.AddItemToCartUcReq;
import x10.zenfit.mvp.service.service.cartService.addItemToCartUc.AddItemToCartUcResp;
import x10.zenfit.mvp.service.service.cartService.addItemToCartUc.IAddItemToCartUc;
import x10.zenfit.mvp.service.service.cartService.clearCartUc.IClearCartUc;
import x10.zenfit.mvp.service.service.cartService.getCartUc.GetCartUcResp;
import x10.zenfit.mvp.service.service.cartService.getCartUc.IGetCartUc;
import x10.zenfit.mvp.service.service.cartService.removeItemFromCartUc.IRemoveItemFromCartUc;
import x10.zenfit.mvp.service.service.cartService.updateCartItemUc.IUpdateCartItemUc;
import x10.zenfit.mvp.service.service.cartService.updateCartItemUc.UpdateCartItemUcReq;
import x10.zenfit.mvp.service.service.cartService.updateCartItemUc.UpdateCartItemUcResp;

@Service
@RequiredArgsConstructor
public class CoreCartServiceImpl implements ICoreCartService {

    private final IGetCartUc getCartUc;
    private final IAddItemToCartUc addItemToCartUc;
    private final IUpdateCartItemUc updateCartItemUc;
    private final IRemoveItemFromCartUc removeItemFromCartUc;
    private final IClearCartUc clearCartUc;

    @Override
    public GetCartUcResp getCart(String userId) {
        return getCartUc.execute(userId);
    }

    @Override
    public AddItemToCartUcResp addItem(String userId, AddItemToCartUcReq req) {
        return addItemToCartUc.execute(userId, req);
    }

    @Override
    public UpdateCartItemUcResp updateItem(String userId, String productId, UpdateCartItemUcReq req) {
        return updateCartItemUc.execute(userId, productId, req);
    }

    @Override
    public String removeItem(String userId, String productId) {
        return removeItemFromCartUc.execute(userId, productId);
    }

    @Override
    public String clearCart(String userId) {
        return clearCartUc.execute(userId);
    }
}

