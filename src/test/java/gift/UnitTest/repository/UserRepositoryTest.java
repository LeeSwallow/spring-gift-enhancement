package gift.UnitTest.repository;

import gift.entity.Role;
import gift.entity.User;
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
                new Role("ROLE_USER"),
                new Role("ROLE_MD"),
                new Role("ROLE_ADMIN")
        ));
    }

    @Test
    @Order(1)
    @DisplayName("단건 유저 저장 테스트")
    public void save_user_test() {
        User user = new User();
        user.setEmail("test1234@test.com");
        user.setPassword("test1234!");
        user.setRoles(List.of(roleRepository.findByName("ROLE_USER").orElseThrow()));
        User savedUser = userRepository.save(user);

        Assertions.assertAll(
                () -> Assertions.assertNotNull(savedUser.getId(), "저장된 유저의 ID는 null이 아니어야 합니다."),
                () -> Assertions.assertEquals(savedUser.getEmail(), user.getEmail(), "저장된 유저의 이메일이 일치해야 합니다."),
                () -> Assertions.assertEquals(savedUser.getPassword(), user.getPassword(), "저장된 유저의 비밀번호가 일치해야 합니다."),
                () -> Assertions.assertFalse(savedUser.getRoles().isEmpty(), "저장된 유저는 최소한 하나의 역할을 가져야 합니다."),
                () -> Assertions.assertEquals("ROLE_USER", savedUser.getRoles().getFirst().getName(), "저장된 유저의 역할이 ROLE_USER여야 합니다.")
        );
    }

    @Test
    @Order(2)
    @DisplayName("유저 ID로 조회 테스트")
    public void findById_test() {
        User user = new User();
        user.setEmail("test1234@test.com");
        user.setPassword("test1234!");
        user.setRoles(List.of(roleRepository.findByName("ROLE_USER").orElseThrow()));
        User savedUser = userRepository.save(user);

        User foundUser = userRepository.findById(savedUser.getId()).orElse(null);
        Assertions.assertNotNull(foundUser, "저장된 유저를 조회해야 합니다.");
        Assertions.assertAll(
                () -> Assertions.assertEquals(savedUser.getId(), foundUser.getId(), "조회된 유저의 ID가 일치해야 합니다."),
                () -> Assertions.assertEquals(savedUser.getEmail(), foundUser.getEmail(), "조회된 유저의 이메일이 일치해야 합니다."),
                () -> Assertions.assertEquals(savedUser.getPassword(), foundUser.getPassword(), "조회된 유저의 비밀번호가 일치해야 합니다."),
                () -> Assertions.assertFalse(foundUser.getRoles().isEmpty(), "조회된 유저는 최소한 하나의 역할을 가져야 합니다."),
                () -> Assertions.assertEquals("ROLE_USER", foundUser.getRoles().getFirst().getName(), "조회된 유저의 역할이 ROLE_USER여야 합니다.")
        );
    }

    @Test
    @Order(3)
    @DisplayName("유저 이메일로 조회 테스트")
    public void findByEmail_test() {
        User user = new User();
        user.setEmail("test1234@test.com");
        user.setPassword("test1234!");
        user.setRoles(List.of(roleRepository.findByName("ROLE_USER").orElseThrow()));
        User savedUser = userRepository.save(user);

        User foundUser = userRepository.findByEmail(savedUser.getEmail()).orElse(null);
        Assertions.assertNotNull(foundUser, "저장된 유저를 조회해야 합니다.");
        Assertions.assertAll(
                () -> Assertions.assertEquals(savedUser.getId(), foundUser.getId(), "조회된 유저의 ID가 일치해야 합니다."),
                () -> Assertions.assertEquals(savedUser.getEmail(), foundUser.getEmail(), "조회된 유저의 이메일이 일치해야 합니다."),
                () -> Assertions.assertEquals(savedUser.getPassword(), foundUser.getPassword(), "조회된 유저의 비밀번호가 일치해야 합니다."),
                () -> Assertions.assertFalse(foundUser.getRoles().isEmpty(), "조회된 유저는 최소한 하나의 역할을 가져야 합니다."),
                () -> Assertions.assertEquals("ROLE_USER", foundUser.getRoles().getFirst().getName(), "조회된 유저의 역할이 ROLE_USER여야 합니다.")
        );
    }

    @Test
    @Order(4)
    @DisplayName("유저 정보 수정 테스트")
    public void update_user_test() {
        User user = new User();
        user.setEmail("test1234@test.com");
        user.setPassword("test1234!");
        user.setRoles(List.of(roleRepository.findByName("ROLE_USER").orElseThrow()));
        User savedUser = userRepository.save(user);
        // 유저 정보 수정
        savedUser.setEmail("modified1234@test.com");
        savedUser.setPassword("modified1234!");
        var roles = new ArrayList<Role>(List.of(roleRepository.findByName("ROLE_MD").orElseThrow()));
        savedUser.setRoles(roles);
        User updatedUser = userRepository.save(savedUser);
        Assertions.assertAll(
                () -> Assertions.assertEquals(updatedUser.getEmail(), savedUser.getEmail(), "수정된 유저의 이메일이 일치해야 합니다."),
                () -> Assertions.assertEquals(updatedUser.getPassword(), savedUser.getPassword(), "수정된 유저의 비밀번호가 일치해야 합니다."),
                () -> Assertions.assertFalse(updatedUser.getRoles().isEmpty(), "수정된 유저는 최소한 하나의 역할을 가져야 합니다."),
                () -> Assertions.assertEquals("ROLE_MD", updatedUser.getRoles().getFirst().getName(), "수정된 유저의 역할이 ROLE_MD여야 합니다.")
        );
    }

    @Test
    @Order(5)
    @DisplayName("유저 삭제 테스트")
    public void delete_user_test() {
        User user = new User();
        user.setEmail("test1234@test.com");
        user.setPassword("test1234!");
        user.setRoles(List.of(roleRepository.findByName("ROLE_USER").orElseThrow()));
        User savedUser = userRepository.save(user);
        // 저장된 유저 삭제
        userRepository.deleteById(savedUser.getId());

        User foundUser = userRepository.findById(savedUser.getId()).orElse(null);
        Assertions.assertNull(foundUser, "삭제된 유저는 조회되지 않아야 합니다.");
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
            user.setRoles(List.of(roleRepository.findByName("ROLE_USER").orElseThrow()));
            userRepository.save(user);
        }

        // 페이지네이션 조회
        var pageable = PageRequest.of(0, 5);
        var page = userRepository.findAllBy(pageable);

        Assertions.assertAll(
                () -> Assertions.assertEquals(5, page.getContent().size(), "첫 페이지는 5개의 유저를 포함해야 합니다."),
                () -> Assertions.assertTrue(page.hasNext(), "다음 페이지가 있어야 합니다."),
                () -> Assertions.assertEquals(10, page.getTotalElements(), "전체 유저 수는 10이어야 합니다.")
        );
    }
}
