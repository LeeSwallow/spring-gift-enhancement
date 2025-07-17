package gift.common.validation.validator;

import gift.common.model.SortDirection;
import gift.common.validation.annotation.SortParam;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class SortParamValidator implements ConstraintValidator<SortParam, List<String>> {
    private String[] allowedFields;

    @Override
    public void initialize(SortParam constraintAnnotation) {
        allowedFields = constraintAnnotation.allowedFields();
    }

    @Override
    public boolean isValid(List<String> value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true; // 빈 값은 유효하다고 간주
        }
        HashSet<String> fieldsSet = new HashSet<>(Arrays.asList(allowedFields));
        boolean hasValidField = false;
        for (String param : value) {
            if (SortDirection.contains(param)) {
                if (!hasValidField) {
                    return false;
                }
                hasValidField = false;
            } else if (fieldsSet.contains(param)) {
                hasValidField = true;
                fieldsSet.remove(param); // 중복 방지
            } else {
                return false;
            }
        }
        return true;
    }
}
