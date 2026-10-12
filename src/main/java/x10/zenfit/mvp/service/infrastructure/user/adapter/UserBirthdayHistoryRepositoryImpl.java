package x10.zenfit.mvp.service.infrastructure.user.adapter;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import x10.zenfit.mvp.service.Repository.IUserBirthdayHistoryRepository;
import x10.zenfit.mvp.service.common.domain.entities.user.UserBirthdayHistoryEntity;
import x10.zenfit.mvp.service.infrastructure.user.adapter.mapper.UserBirthdayHistoryMapper;
import x10.zenfit.mvp.service.infrastructure.user.db.mongodb.UserBirthdayHistoryMongoRepository;

@Repository
@RequiredArgsConstructor
public class UserBirthdayHistoryRepositoryImpl implements IUserBirthdayHistoryRepository {

    private final UserBirthdayHistoryMongoRepository userBirthdayHistoryMongoRepository;

    @Override
    public UserBirthdayHistoryEntity save(UserBirthdayHistoryEntity history) {
        return UserBirthdayHistoryMapper.toEntity(
                userBirthdayHistoryMongoRepository.save(UserBirthdayHistoryMapper.toDocument(history))
        );
    }
}
