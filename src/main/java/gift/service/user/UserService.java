package gift.service.user;

import gift.entity.User;
import gift.common.model.CustomPage;

public interface UserService {
    CustomPage<User> findAllBy(int page, int size);
    User findById(Long userId);
    User findByEmail(String email);
    User create(User user);
    User update(User user);
    void deleteById(Long userId);
    Boolean existsById(Long userId);
    User getReference(Long userId);
}
