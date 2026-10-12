package x10.zenfit.mvp.service.service.cartService.updateCartItemUc;

public interface IUpdateCartItemUc {
    UpdateCartItemUcResp execute(String userId, String productId, UpdateCartItemUcReq req);
}

