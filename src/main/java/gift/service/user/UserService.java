package gift.service.user;

import gift.entity.User;
import gift.common.model.CustomPage;

import java.util.List;

public interface UserService {
    CustomPage<User> findAllBy(int page, int size);
    CustomPage<User> findAllBy(int page, int size, List<String> sortBy);
    User findById(Long userId);
    User findByEmail(String email);
    User create(User user);
    User update(User user);
    void deleteById(Long userId);
    Boolean existsById(Long userId);
    User getReference(Long userId);
}
