package gift.dto.user;

import gift.common.validation.group.AuthenticationGroups;
import gift.entity.Role;
import gift.entity.User;
import gift.common.validation.annotation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Null;

import java.util.List;
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
                List<Role> mappedRoles = null;
                if (roles != null) {
                        mappedRoles = roles.stream()
                                .map(Role::new)
                                .collect(Collectors.toList());
                }
                var user = new User();
                user.setEmail(email);
                user.setPassword(password);
                user.setRoles(mappedRoles);
                return user;
        }
}
