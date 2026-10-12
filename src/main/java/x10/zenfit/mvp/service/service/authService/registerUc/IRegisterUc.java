package x10.zenfit.mvp.service.service.authService.registerUc;

import x10.zenfit.mvp.service.service.userService.createUserUc.CreateUserReq;

public interface IRegisterUc {
    RegisterResponse register(CreateUserReq req);
}
