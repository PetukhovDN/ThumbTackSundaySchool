package net.thumbtack.school.notes.validator;

import net.thumbtack.school.notes.validator.impl.SessionLifeTimeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SessionLifeTimeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SessionLifeTime {
    String message() default "Session expired";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
