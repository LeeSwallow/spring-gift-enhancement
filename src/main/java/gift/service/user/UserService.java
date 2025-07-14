package gift.service.user;

import gift.entity.User;
import gift.common.model.CustomPage;
import java.util.List;

public interface UserService {
    List<User> getAll();
    CustomPage<User> getBy(int page, int size);
    User loadRoles(User user);
    User getById(Long userId);
    User create(User user);
    User update(User user);
    void deleteById(Long userId);
}
