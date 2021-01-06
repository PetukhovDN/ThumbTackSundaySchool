package net.thumbtack.school.notes.validator.impl;

import net.thumbtack.school.notes.validator.UserPatronymic;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * User patronymic validation during registration
 */
public class UserPatronymicValidator implements ConstraintValidator<UserPatronymic, String> {

    /**
     * Maximum user patronymic length
     * Set in application.properties
     */
    @Value("${max_name_length}")
    private int maxNameLength;

    /**
     * @param userPatronymic cannot be null, empty or shorter min_password_length, user can only use
     *                       letters of the Russian and English alphabets and space and dash characters
     * @return success if patronymic is valid
     */
    @Override
    public boolean isValid(String userPatronymic, ConstraintValidatorContext constraintValidatorContext) {
        String regex = "^[a-zA-Zа-яА-ЯёЁ\\s.'-]+$";
        return userPatronymic != null
                && userPatronymic.length() <= maxNameLength
                && (userPatronymic.matches(regex) || userPatronymic.isBlank());
    }
}
