package gift.dao.user;

import gift.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    @Deprecated
    List<User> findAll();
    List<User> findAll(int page, int size);
    Optional<User> findById(Long userId);
    Optional<User> findByEmail(String email);
    Long insertWithKey(User user);
    Integer update(User user);
    Integer updateFieldById(Long userId, String fieldName, Object value);
    Integer deleteById(Long userId);
    Integer count();
}
