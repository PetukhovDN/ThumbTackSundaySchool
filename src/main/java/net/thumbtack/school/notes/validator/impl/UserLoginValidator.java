package net.thumbtack.school.notes.validator.impl;

import net.thumbtack.school.notes.validator.UserLogin;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * User login validation during registration.
 * Cannot be null, empty and longer max_name_length.
 * User can only use letters of the Russian and English alphabets and numbers.
 */
public class UserLoginValidator implements ConstraintValidator<UserLogin, String> {

    @Value("${max_name_length}")
    private long max_name_length;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        String regex = "^[a-zA-Zа-яА-ЯёЁ0-9]+$";
        return s != null
                && !s.isBlank()
                && s.length() <= max_name_length
                && s.matches(regex);
    }
}
