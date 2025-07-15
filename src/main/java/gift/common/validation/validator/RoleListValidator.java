package gift.common.validation.validator;

import gift.common.validation.annotation.ValidRoleList;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.List;
import java.util.regex.Pattern;

public class RoleListValidator implements ConstraintValidator<ValidRoleList, List<String>> {
    Pattern rolePattern = Pattern.compile("^(ROLE_USER|ROLE_MD|ROLE_ADMIN)$");

    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // null 또는 빈 리스트는 유효하다고 간주
        }
        for (String role : value) {
            if (role == null || !rolePattern.matcher(role).matches()) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate("역할은 ROLE_USER, ROLE_MD, ROLE_ADMIN 중 하나여야 합니다.").addConstraintViolation();
                return false;
            }
        }
        return true;
    }
}
