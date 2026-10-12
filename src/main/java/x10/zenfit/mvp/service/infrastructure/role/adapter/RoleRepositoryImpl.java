package x10.zenfit.mvp.service.infrastructure.role.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.Repository.IRoleRepository;
import x10.zenfit.mvp.service.common.domain.entities.user.RoleEntity;
import x10.zenfit.mvp.service.infrastructure.role.adapter.mapper.RoleMapper;
import x10.zenfit.mvp.service.infrastructure.role.db.mongodb.RoleDocument;
import x10.zenfit.mvp.service.infrastructure.role.db.mongodb.RoleMongoRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoleRepositoryImpl implements IRoleRepository {

    private final RoleMongoRepository roleMongoRepository;

    @Override
    public RoleEntity save(RoleEntity role) {

        RoleDocument document = RoleMapper.toDocument(role);

        RoleDocument savedDocument = roleMongoRepository.save(document);

        return RoleMapper.toEntity(savedDocument);
    }

    @Override
    public Optional<RoleEntity> findByCode(String code) {

        return roleMongoRepository.findByCode(code)
                .map(RoleMapper::toEntity);
    }

    @Override
    public Optional<RoleEntity> findById(String id) {

        return roleMongoRepository.findById(id)
                .map(RoleMapper::toEntity);
    }

    @Override
    public List<RoleEntity> findAllById(List<String> ids) {

        return roleMongoRepository.findAllById(ids)
                .stream()
                .map(RoleMapper::toEntity)
                .toList();
    }

    @Override
    public boolean existsByCode(String code) {

        return roleMongoRepository.existsByCode(code);
    }

    @Override
    public List<RoleEntity> findAll() {

        return roleMongoRepository.findAll()
                .stream()
                .map(RoleMapper::toEntity)
                .toList();
    }
}
