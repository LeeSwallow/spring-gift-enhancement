package gift.service.user;

import gift.common.util.PasswordEncoder;
import gift.entity.User;
import gift.entity.UserRole;
import gift.common.model.CustomPage;
import gift.repository.role.RoleRepository;
import gift.repository.user.UserRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository userRoleRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    void validateUser(User user) {
        if (user == null || user.getId() == null || user.getEmail() == null) {
            throw new IllegalArgumentException("사용자 정보가 유효하지 않습니다.");
        }
    }

    @Override
    @Transactional
    public User loadRoles(User user) {
        validateUser(user);
        Set<UserRole> roles = userRoleRepository.findByUserId(user.getId());
        if (roles == null || roles.isEmpty()) {
            throw new IllegalStateException("사용자의 역할을 찾을 수 없습니다. 사용자 ID: " + user.getId());
        }
        user.setRoles(roles);
        return user;
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }


    @Override
    public CustomPage<User> getBy(int page, int size) {
        return CustomPage.convert(userRepository.findAll(page, size), this::loadRoles);
    }

    @Override
    @Transactional
    public User getById(Long userId) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 사용자를 찾을 수 없습니다. : " + userId));
        return loadRoles(user);
    }

    @Override
    @Transactional
    public User create(User user) {
        if (user == null || user.getEmail() == null || user.getPassword() == null ||
                user.getRoles() == null || user.getRoles().isEmpty()) {
            throw new IllegalArgumentException("사용자 정보가 유효하지 않습니다.");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()){
            throw new DuplicateKeyException("이미 존재하는 이메일입니다: " + user.getEmail());
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        for (UserRole role : user.getRoles()) {
            userRoleRepository.save(savedUser.getId(), role);
        }

        return loadRoles(savedUser);
    }

    @Override
    @Transactional
    public User update(User user) {
        if (user == null || user.getId() == null) {
            throw new IllegalArgumentException("업데이트할 사용자 정보가 유효하지 않습니다.");
        }
        User existingUser = getById(user.getId());
        if (user.getPassword() != null) {
            existingUser.setPassword(user.getPassword());
        }

        if (user.getEmail() != null) {
            if (userRepository.findByEmail(user.getEmail()).isPresent()) {
                throw new DuplicateKeyException("이미 존재하는 이메일입니다: " + user.getEmail());
            }
            existingUser.setEmail(user.getEmail());
        }

        if (user.getRoles() != null) {
            if (user.getRoles().isEmpty()) {
                throw new IllegalArgumentException("사용자의 역할은 비어있을 수 없습니다.");
            }
            if(!userRoleRepository.sync(user.getId(), user.getRoles())) {
                throw new IllegalStateException("사용자의 역할을 업데이트하는데 실패했습니다. 사용자 ID: " + user.getId());
            }
        }
        User updatedUser = userRepository.save(existingUser);

        return loadRoles(updatedUser);
    }

    @Override
    @Transactional
    public void deleteById(Long userId) {
        getById(userId);
        if (!userRepository.deleteById(userId)) {
            throw new IllegalStateException("사용자를 삭제하는데 실패했습니다. 사용자 ID: " + userId);
        }
    }
}
