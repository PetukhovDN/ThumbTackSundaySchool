package net.thumbtack.school.notes.validator;

import net.thumbtack.school.notes.validator.impl.SectionNameValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = SectionNameValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface SectionName {
    String message() default "Invalid section name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
