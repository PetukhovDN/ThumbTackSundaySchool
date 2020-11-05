package net.thumbtack.school.notes.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserNameValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UserName {
    String message() default "Invalid user name";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
