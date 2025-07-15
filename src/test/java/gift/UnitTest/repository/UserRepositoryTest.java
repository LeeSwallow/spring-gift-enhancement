package gift.UnitTest.repository;

import gift.entity.Role;
import gift.entity.User;
import gift.entity.UserRole;
import gift.repository.role.RoleRepository;
import gift.repository.user.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

public class UserRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    public void setUp() {
        roleRepository.saveAll(List.of(
                new Role(UserRole.ROLE_USER),
                new Role(UserRole.ROLE_MD),
                new Role(UserRole.ROLE_ADMIN)
        ));
    }

    @Test
    @Order(1)
    @DisplayName("단건 유저 저장 테스트")
    public void save_user_test() {
        User user = new User();
        user.setEmail("test1234@test.com");
        user.setPassword("test1234!");
        user.setRoles(List.of(roleRepository.findByName(UserRole.ROLE_USER).orElseThrow()));
        User savedUser = userRepository.save(user);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedUser.getId()),
                () -> Assertions.assertEquals(savedUser.getEmail(), user.getEmail()),
                () -> Assertions.assertEquals(savedUser.getPassword(), user.getPassword()),
                () -> Assertions.assertFalse(savedUser.getRoles().isEmpty()),
                () -> Assertions.assertEquals(UserRole.ROLE_USER, savedUser.getRoles().getFirst().getName())
        );
    }

    @Test
    @Order(2)
    @DisplayName("유저 ID로 조회 테스트")
    public void findById_test() {
        User user = new User();
        user.setEmail("test1234@test.com");
        user.setPassword("test1234!");
        user.setRoles(List.of(roleRepository.findByName(UserRole.ROLE_USER).orElseThrow()));
        User savedUser = userRepository.save(user);

        User foundUser = userRepository.findById(savedUser.getId()).orElse(null);
        Assertions.assertNotNull(foundUser);
        Assertions.assertAll(
                () -> Assertions.assertEquals(savedUser.getId(), foundUser.getId()),
                () -> Assertions.assertEquals(savedUser.getEmail(), foundUser.getEmail()),
                () -> Assertions.assertEquals(savedUser.getPassword(), foundUser.getPassword()),
                () -> Assertions.assertFalse(foundUser.getRoles().isEmpty()),
                () -> Assertions.assertEquals(UserRole.ROLE_USER, foundUser.getRoles().getFirst().getName())
        );
    }

    @Test
    @Order(3)
    @DisplayName("유저 이메일로 조회 테스트")
    public void findByEmail_test() {
        User user = new User();
        user.setEmail("test1234@test.com");
        user.setPassword("test1234!");
        user.setRoles(List.of(roleRepository.findByName(UserRole.ROLE_USER).orElseThrow()));
        User savedUser = userRepository.save(user);

        User foundUser = userRepository.findByEmail(savedUser.getEmail()).orElse(null);
        Assertions.assertNotNull(foundUser);
        Assertions.assertAll(
                () -> Assertions.assertEquals(savedUser.getId(), foundUser.getId()),
                () -> Assertions.assertEquals(savedUser.getEmail(), foundUser.getEmail()),
                () -> Assertions.assertEquals(savedUser.getPassword(), foundUser.getPassword()),
                () -> Assertions.assertFalse(foundUser.getRoles().isEmpty()),
                () -> Assertions.assertEquals(UserRole.ROLE_USER, foundUser.getRoles().getFirst().getName())
        );
    }

    @Test
    @Order(4)
    @DisplayName("유저 정보 수정 테스트")
    public void update_user_test() {
        User user = new User();
        user.setEmail("test1234@test.com");
        user.setPassword("test1234!");
        user.setRoles(List.of(roleRepository.findByName(UserRole.ROLE_USER).orElseThrow()));
        User savedUser = userRepository.save(user);
        // 유저 정보 수정
        savedUser.setEmail("modified1234@test.com");
        savedUser.setPassword("modified1234!");
        var roles = new ArrayList<Role>(List.of(roleRepository.findByName(UserRole.ROLE_MD).orElseThrow()));
        savedUser.setRoles(roles);
        User updatedUser = userRepository.save(savedUser);
        Assertions.assertAll(
                () -> Assertions.assertEquals(updatedUser.getEmail(), savedUser.getEmail()),
                () -> Assertions.assertEquals(updatedUser.getPassword(), savedUser.getPassword()),
                () -> Assertions.assertFalse(updatedUser.getRoles().isEmpty()),
                () -> Assertions.assertEquals(UserRole.ROLE_MD, updatedUser.getRoles().getFirst().getName())
        );
    }

    @Test
    @Order(5)
    @DisplayName("유저 삭제 테스트")
    public void delete_user_test() {
        User user = new User();
        user.setEmail("test1234@test.com");
        user.setPassword("test1234!");
        user.setRoles(List.of(roleRepository.findByName(UserRole.ROLE_USER).orElseThrow()));
        User savedUser = userRepository.save(user);
        // 저장된 유저 삭제
        userRepository.deleteById(savedUser.getId());

        User foundUser = userRepository.findById(savedUser.getId()).orElse(null);
        Assertions.assertNull(foundUser);
    }

    @Test
    @Order(6)
    @DisplayName("페이지네이션 테스트")
    public void findAllByPageable_test() {
        // 여러 유저 저장
        for (int i = 0; i < 10; i++) {
            User user = new User();
            user.setEmail("test" + i + "@test.com");
            user.setPassword("test" + i + "!");
            user.setRoles(List.of(roleRepository.findByName(UserRole.ROLE_USER).orElseThrow()));
            userRepository.save(user);
        }

        // 페이지네이션 조회
        var pageable = PageRequest.of(0, 5);
        var page = userRepository.findAllBy(pageable);

        Assertions.assertAll(
                () -> Assertions.assertEquals(5, page.getContent().size()),
                () -> Assertions.assertTrue(page.hasNext())
        );
    }
}
