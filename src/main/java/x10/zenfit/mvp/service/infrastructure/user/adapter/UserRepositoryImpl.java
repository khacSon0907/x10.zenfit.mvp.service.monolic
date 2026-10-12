package x10.zenfit.mvp.service.infrastructure.user.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.Repository.IUserRepository;
import x10.zenfit.mvp.service.common.domain.entities.user.UserEntity;
import x10.zenfit.mvp.service.infrastructure.user.adapter.mapper.UserMapper;
import x10.zenfit.mvp.service.infrastructure.user.db.mongodb.UserDocument;
import x10.zenfit.mvp.service.infrastructure.user.db.mongodb.UserMongoRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements IUserRepository {

    private final UserMongoRepository userMongoRepository;

    @Override
    public UserEntity save(UserEntity user) {

        UserDocument document = UserMapper.toDocument(user);

        UserDocument savedDocument = userMongoRepository.save(document);

        return UserMapper.toEntity(savedDocument);
    }

    @Override
    public Optional<UserEntity> findByEmail(String email) {

        return userMongoRepository.findByEmail(email)
                .map(UserMapper::toEntity);
    }

    @Override
    public Optional<UserEntity> findById(String id) {

        return userMongoRepository.findById(id)
                .map(UserMapper::toEntity);
    }

    @Override
    public List<UserEntity> findAll() {

        return userMongoRepository.findAll()
                .stream()
                .map(UserMapper::toEntity)
                .toList();
    }
}