package gift.service.user;

import gift.entity.User;
import gift.common.model.CustomPage;

public interface UserService {
    CustomPage<User> getBy(int page, int size);
    User getById(Long userId);
    User create(User user);
    User update(User user);
    void deleteById(Long userId);
}
