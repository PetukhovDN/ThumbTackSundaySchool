package net.thumbtack.school.notes.validator.impl;

import net.thumbtack.school.notes.validator.UserPatronymic;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * User patronymic validation during registration.
 * Cannot be null, empty or shorter min_password_length.
 * User can only use letters of the Russian and English alphabets and space and dash characters.
 */
public class UserPatronymicValidator implements ConstraintValidator<UserPatronymic, String> {

    /**
     * Maximum user patronymic length
     * Set in application.properties
     */
    @Value("${max_name_length}")
    private long maxNameLength;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        String regex = "^[a-zA-Zа-яА-ЯёЁ\\s.'-]+$";
        return s != null
                && s.length() <= maxNameLength
                && (s.matches(regex) || s.isBlank());
    }
}
