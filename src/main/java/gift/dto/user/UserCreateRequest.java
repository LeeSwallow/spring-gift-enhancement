package gift.dto.user;

import gift.entity.User;
import gift.common.validation.annotation.ValidPassword;
import gift.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.util.Set;

public record UserCreateRequest(
        @NotNull(message = "이메일은 필수입니다.")
        @Email(message = "이메일 형식이 올바르지 않습니다.")
        String email,
        @NotNull(message = "비밀번호는 필수입니다.")
        @ValidPassword
        String password,
        @Pattern(
           regexp = "^(ROLE_USER|ROLE_MD|ROLE_ADMIN)$",
           message = "역할은 ROLE_USER, ROLE_MD, ROLE_ADMIN 중 하나여야 합니다."
        )
        String role
) {
        public UserCreateRequest {
                if (role == null || role.isBlank()) {
                        role = "ROLE_USER"; // 기본 역할을 ROLE_USER로 설정
                }
        }

        public User toEntity() {
                Set<UserRole> roles = Set.of(UserRole.fromString(role));
                return new User(null, email, password, roles);
        }
}
