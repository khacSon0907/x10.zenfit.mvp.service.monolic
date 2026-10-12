package x10.zenfit.mvp.service.service.cartService;

import x10.zenfit.mvp.service.service.cartService.addItemToCartUc.AddItemToCartUcReq;
import x10.zenfit.mvp.service.service.cartService.addItemToCartUc.AddItemToCartUcResp;
import x10.zenfit.mvp.service.service.cartService.getCartUc.GetCartUcResp;
import x10.zenfit.mvp.service.service.cartService.updateCartItemUc.UpdateCartItemUcReq;
import x10.zenfit.mvp.service.service.cartService.updateCartItemUc.UpdateCartItemUcResp;

public interface ICoreCartService {
    GetCartUcResp getCart(String userId);
    AddItemToCartUcResp addItem(String userId, AddItemToCartUcReq req);
    UpdateCartItemUcResp updateItem(String userId, String productId, UpdateCartItemUcReq req);
    String removeItem(String userId, String productId);
    String clearCart(String userId);
}

