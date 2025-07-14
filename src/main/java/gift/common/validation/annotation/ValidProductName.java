package gift.common.validation.annotation;

import gift.common.validation.validator.ProductNameValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = ProductNameValidator.class)
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidProductName {
    String message() default "상품명은 문자, 숫자, 공백, 특수문자( ( ), [ ], +, -, &, /, _)만 허용됩니다.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}