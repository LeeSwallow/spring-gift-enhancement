package gift.common.validation.validator;

import gift.common.validation.annotation.AllowedSortFields;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import java.util.Set;

public class AllowedSortFieldsValidator implements ConstraintValidator<AllowedSortFields, Pageable> {
    private String[] allowedFields;
    private String message;
    private boolean showAllowedFields;

    @Override
    public void initialize(AllowedSortFields constraintAnnotation) {
        this.allowedFields = constraintAnnotation.value();
        this.message = constraintAnnotation.message();
        this.showAllowedFields = constraintAnnotation.showAllowedFields();
    }

    @Override
    public boolean isValid(Pageable pageable, ConstraintValidatorContext context) {
        if (pageable == null || pageable.getSort().isUnsorted() || pageable.getSort().isEmpty()) {
            return true; // pageable이 null 이거나 정렬이 없는 경우 유효함
        }

        Set<String> fieldsSet = Set.of(allowedFields);

        for(Sort.Order order : pageable.getSort()) {
            String field = order.getProperty();
            if (!fieldsSet.contains(field)) {
                if (showAllowedFields) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(
                        String.format("%s 허용된 필드: %s", message, String.join(", ", allowedFields))
                    ).addConstraintViolation();
                }
                return false; // 허용되지 않는 필드가 있는 경우 유효하지 않음
            }
        }
        return true; // 모든 필드가 허용된 경우 유효함
    }
}
