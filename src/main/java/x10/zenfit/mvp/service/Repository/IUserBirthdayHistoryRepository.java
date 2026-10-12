package x10.zenfit.mvp.service.Repository;

import x10.zenfit.mvp.service.common.domain.entities.user.UserBirthdayHistoryEntity;

public interface IUserBirthdayHistoryRepository {
    UserBirthdayHistoryEntity save(UserBirthdayHistoryEntity history);
}
