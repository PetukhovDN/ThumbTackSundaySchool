package net.thumbtack.school.notes.validator.impl;

import net.thumbtack.school.notes.validator.UserPassword;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * User password validation during registration.
 * Cannot be null, empty or shorter min_password_length.
 */
public class UserPasswordValidator implements ConstraintValidator<UserPassword, String> {

    /**
     * Minimum user password length
     * Set in application.properties
     */
    @Value("${min_password_length}")
    private long minPasswordLength;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null
                && !s.isBlank()
                && s.length() >= minPasswordLength;
    }
}
