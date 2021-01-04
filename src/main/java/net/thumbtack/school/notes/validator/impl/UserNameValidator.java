package net.thumbtack.school.notes.validator.impl;

import net.thumbtack.school.notes.validator.UserName;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * User firstname and lastname validation during registration.
 * Cannot be null or longer max_name_length.
 * User can only use letters of the Russian and English alphabets and space and dash characters.
 */
public class UserNameValidator implements ConstraintValidator<UserName, String> {

    /**
     * Maximum user first name and last name length
     * Set in application.properties
     */
    @Value("${max_name_length}")
    private long maxNameLength;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        String regex = "^[a-zA-Zа-яА-ЯёЁ\\s.'-]+$";
        return s != null
                && !s.isBlank()
                && s.length() <= maxNameLength
                && s.matches(regex);
    }
}
