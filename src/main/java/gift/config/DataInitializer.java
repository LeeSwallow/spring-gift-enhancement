package gift.config;

import gift.common.exception.CriticalServerException;
import gift.common.util.PasswordEncoder;
import gift.entity.Role;
import gift.entity.User;
import gift.repository.role.RoleRepository;
import gift.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@DependsOn("serverStartupVerifier")
public class DataInitializer implements CommandLineRunner {
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final String adminEmail;
    private final String adminPassword;


    public DataInitializer(
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository,
            UserRepository userRepository,
            @Value("${gift.admin.email}") String adminEmail,
            @Value("${gift.admin.password}") String adminPassword
    ) {
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.adminEmail = adminEmail;
        this.adminPassword = adminPassword;

    }

    @Override
    public void run(String... args) throws Exception {
        roleRepository.saveAll(List.of
                (new Role("ROLE_GUEST"),
                new Role("ROLE_USER"),
                new Role("ROLE_MD"),
                new Role("ROLE_ADMIN")
        ));
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").orElseThrow(
                () -> new CriticalServerException("roleRepository가 제대로 작동하지 않습니다!")
        );
        User adminUser = new User();
        adminUser.setEmail(adminEmail);
        adminUser.setPassword(passwordEncoder.encode(adminPassword));
        adminUser.setRoles(List.of(adminRole));
        if (userRepository.existsByEmail(adminEmail)) {
            throw new CriticalServerException("이미 사용 중인 관리자 이메일입니다: " + adminEmail);
        }
        userRepository.save(adminUser);
    }
}
