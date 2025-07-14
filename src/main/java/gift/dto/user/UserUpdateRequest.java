package gift.dto.user;

import gift.common.validation.group.AuthenticationGroups;
import gift.entity.User;
import gift.common.validation.annotation.ValidPassword;
import gift.entity.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Null;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public record UserUpdateRequest(
        @Null(message = "Email은 수정할 수 없습니다.", groups = {
                AuthenticationGroups.UserGroup.class, AuthenticationGroups.MdGroup.class})
        @Email(message = "이메일 형식이 올바르지 않습니다.", groups = {AuthenticationGroups.AdminGroup.class})
        String email,
        @ValidPassword
        String password,
        @Null(message = "role은 수정할 수 없습니다.", groups = {
                AuthenticationGroups.UserGroup.class, AuthenticationGroups.MdGroup.class})
        List<String> roles
) {
        public User toEntity() {
                Set<UserRole> mappedRoles = null;
                if (roles != null) {
                        mappedRoles = roles.stream()
                                .map(UserRole::fromString)
                                .collect(Collectors.toSet());
                }
                return new User(null, email, password, mappedRoles);
        }
}
