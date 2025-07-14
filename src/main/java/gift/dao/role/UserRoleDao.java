package gift.dao.role;

import gift.entity.UserRole;

import java.util.Set;

public interface UserRoleDao {
    Set<UserRole> findByUserId(Long userId);
    Boolean exists(Long userId, UserRole role);
    Integer save(Long userId, UserRole role);
    Integer delete(Long userId, UserRole role);
}
