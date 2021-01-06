package net.thumbtack.school.notes.validator.impl;

import net.thumbtack.school.notes.validator.UserPassword;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


/**
 * User password validation during registration
 */
public class UserPasswordValidator implements ConstraintValidator<UserPassword, String> {

    /**
     * Minimum user password length
     * Set in application.properties
     */
    @Value("${min_password_length}")
    private int minPasswordLength;

    /**
     * @param userPassword cannot be null, empty or shorter min_password_length
     * @return success if password is valid
     */
    @Override
    public boolean isValid(String userPassword, ConstraintValidatorContext constraintValidatorContext) {
        return userPassword != null
                && !userPassword.isBlank()
                && userPassword.length() >= minPasswordLength;
    }
}
