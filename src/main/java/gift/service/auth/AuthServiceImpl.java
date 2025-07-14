package gift.service.auth;

import gift.common.exception.UnauthorizedException;
import gift.common.util.PasswordEncoder;
import gift.common.util.TokenProvider;
import gift.entity.User;
import gift.entity.UserRole;
import gift.repository.role.RoleRepository;
import gift.repository.user.UserRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;


    public AuthServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            TokenProvider tokenProvider,
            PasswordEncoder passwordEncoder
        ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;

    }

    private void validateParameters(String email, String password) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수 입력 항목입니다.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수 입력 항목입니다.");
        }
    }

    @Override
    public String login(String email, String password) {
        validateParameters(email, password);
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UnauthorizedException("이메일 또는 비밀번호가 일치하지 않습니다.")
        );
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        Set<UserRole> roles = roleRepository.findByUserId(user.getId());
        return tokenProvider.generateToken(user.getId(), roles);
    }

    @Override
    @Transactional
    public String signup(String email, String password, Set<UserRole> roles) {
        validateParameters(email, password);
        if (roles.isEmpty()) {
            throw new IllegalArgumentException("사용자 역할은 최소 하나 이상이어야 합니다.");
        }
        if (userRepository.findByEmail(email).isPresent()) {
            throw new DuplicateKeyException("이미 사용 중인 이메일입니다: " + email);
        }

        User user = new User(null, email,  passwordEncoder.encode(password));
        User savedUser = userRepository.save(user);
        if (savedUser == null || savedUser.getId() == null) {
            throw new IllegalStateException("사용자 저장에 실패했습니다.");
        }

        for (UserRole role : roles) {
            this.roleRepository.save(savedUser.getId(), role);
        }
        Set<UserRole> updatedRoles = this.roleRepository.findByUserId(savedUser.getId());
        if (!roles.equals(updatedRoles)) {
            throw new IllegalStateException("사용자 역할 저장에 실패했습니다.");
        }

        return tokenProvider.generateToken(savedUser.getId(), updatedRoles);
    }
}
