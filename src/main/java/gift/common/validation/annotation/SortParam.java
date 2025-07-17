package gift.common.validation.annotation;

import gift.common.validation.validator.SortParamValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SortParamValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface SortParam {
    String[] allowedFields() default {};
    String message() default "유효하지 않은 정렬 파라미터입니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
