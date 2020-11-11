package net.thumbtack.school.notes.validator;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * User firstname and lastname validation during registration.
 * Cannot be null or longer max_name_length.
 * User can only use letters of the Russian and English alphabets and space and dash characters.
 */
public class UserNameValidator implements ConstraintValidator<UserName, String> {

    private final String regex = "^[a-zA-Zа-яА-ЯёЁ\\s.'-]+$";
    @Value("${max_name_length}")
    private long max_name_length;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null
                && !s.isBlank()
                && s.length() <= max_name_length
                && s.matches(regex);
    }
}
