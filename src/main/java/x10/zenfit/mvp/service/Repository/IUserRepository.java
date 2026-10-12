package x10.zenfit.mvp.service.Repository;



import x10.zenfit.mvp.service.common.domain.entities.user.UserEntity;

import java.util.List;
import java.util.Optional;

public interface IUserRepository {

    UserEntity save(UserEntity user);

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findById(String id);

    List<UserEntity> findAll();
}