package gift.UnitTest.repository;

import gift.entity.Role;
import gift.repository.role.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RoleRepositoryTest extends AbstractRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;


    @Test
    @DisplayName("간단한 role repository 저장/읽기 테스트")
    public void roleRepositoryTest() {
        String roleName = "ROLE_USER";
        Role role = roleRepository.save(new Role(roleName));
        assertEquals(roleName, role.getName(), "저장된 역할의 이름이 일치해야 합니다.");

        Role foundRole = roleRepository.findByName(roleName).orElse(null);
        Assertions.assertNotNull(foundRole, "저장된 역할을 조회해야 합니다.");
    }
}
