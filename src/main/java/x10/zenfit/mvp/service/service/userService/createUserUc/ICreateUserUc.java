package x10.zenfit.mvp.service.service.userService.createUserUc;

import x10.zenfit.mvp.service.common.domain.entities.user.UserEntity;

public interface ICreateUserUc {

    UserEntity createUser(CreateUserReq user);
}
