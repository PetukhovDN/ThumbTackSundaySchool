package net.thumbtack.school.notes.validator.impl;

import net.thumbtack.school.notes.validator.UserLogin;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * User login validation during registration
 */
public class UserLoginValidator implements ConstraintValidator<UserLogin, String> {

    /**
     * Maximum user login length
     * Set in application.properties
     */
    @Value("${max_name_length}")
    private int maxNameLength;

    /**
     * @param userLogin cannot be null, empty and longer max_name_length, user can only use
     *                  letters of the Russian and English alphabets and numbers
     * @return success if login is valid
     */
    @Override
    public boolean isValid(String userLogin, ConstraintValidatorContext constraintValidatorContext) {
        String regex = "^[a-zA-Zа-яА-ЯёЁ0-9]+$";
        return userLogin != null
                && !userLogin.isBlank()
                && userLogin.length() <= maxNameLength
                && userLogin.matches(regex);
    }
}
