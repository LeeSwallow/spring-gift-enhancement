package gift.dto.user;

import gift.common.validation.annotation.ValidRoleList;
import gift.common.validation.group.AuthenticationGroups;
import gift.entity.User;
import gift.common.validation.annotation.ValidPassword;
import gift.entity.UserRole;
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
        @ValidRoleList(groups = {AuthenticationGroups.AdminGroup.class})
        List<String> roles
) {
        public User toEntity() {
                var user = new User();
                user.setEmail(email);
                user.setPassword(password);
                if (roles != null) {
                        user.setRoles(roles.stream()
                                .map(UserRole::valueOf)
                                .collect(Collectors.toSet()
                        ));
                }
                return user;
        }
}
