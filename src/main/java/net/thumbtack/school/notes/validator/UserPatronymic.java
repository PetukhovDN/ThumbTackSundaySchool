package net.thumbtack.school.notes.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserPatronymicValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserPatronymic {
    String message() default "Invalid user patronymic";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
