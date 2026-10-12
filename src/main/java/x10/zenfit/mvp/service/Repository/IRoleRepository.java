package x10.zenfit.mvp.service.Repository;

import x10.zenfit.mvp.service.common.domain.entities.user.RoleEntity;

import java.util.List;
import java.util.Optional;

public interface IRoleRepository {

    RoleEntity save(RoleEntity role);

    Optional<RoleEntity> findByCode(String code);

    Optional<RoleEntity> findById(String id);

    List<RoleEntity> findAllById(List<String> ids);

    boolean existsByCode(String code);

    List<RoleEntity> findAll();
}
