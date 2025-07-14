package gift.repository.role;

import gift.entity.UserRole;
import java.util.Set;

public interface RoleRepository {
    Set<UserRole> findByUserId(Long userId);
    Boolean exists(Long userId, UserRole role);
    Boolean save(Long userId, UserRole role);
    Boolean sync(Long userId, Set<UserRole> roles);
    Boolean delete(Long userId, UserRole role);
}
