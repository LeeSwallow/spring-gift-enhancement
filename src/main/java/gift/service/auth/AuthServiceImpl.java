package gift.service.auth;

import gift.common.exception.UnauthorizedException;
import gift.common.util.PasswordEncoder;
import gift.common.util.TokenProvider;
import gift.entity.User;
import gift.entity.UserRole;
import gift.repository.user.UserRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;


    public AuthServiceImpl(
            UserRepository userRepository,
            TokenProvider tokenProvider,
            PasswordEncoder passwordEncoder
        ) {
        this.userRepository = userRepository;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;

    }


    @Override
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UnauthorizedException("이메일 또는 비밀번호가 일치하지 않습니다."));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
        return tokenProvider.generateToken(user.getId(), user.getRoles());
    }

    @Override
    @Transactional
    public String signup(String email, String password, Set<UserRole> roles) {
        if (roles.isEmpty()) {
            throw new IllegalArgumentException("사용자 역할은 최소 하나 이상이어야 합니다.");
        }
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateKeyException("이미 사용 중인 이메일입니다: " + email);
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRoles(roles.stream().map(UserRole::toRole).collect(Collectors.toList()));
        User savedUser = userRepository.save(user);

        return tokenProvider.generateToken(savedUser.getId(), savedUser.getRoles());
    }
}
