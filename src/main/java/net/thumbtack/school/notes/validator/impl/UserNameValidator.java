package net.thumbtack.school.notes.validator.impl;

import net.thumbtack.school.notes.validator.UserName;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * User firstname and lastname validation during registration
 */
public class UserNameValidator implements ConstraintValidator<UserName, String> {

    /**
     * Maximum user first name and last name length
     * Set in application.properties
     */
    @Value("${max_name_length}")
    private int maxNameLength;

    /**
     * @param userName cannot be null or longer max_name_length, user can only use
     *                 letters of the Russian and English alphabets and space and dash characters
     * @return success if name is valid
     */
    @Override
    public boolean isValid(String userName, ConstraintValidatorContext constraintValidatorContext) {
        String regex = "^[a-zA-Zа-яА-ЯёЁ\\s.'-]+$";
        return userName != null
                && !userName.isBlank()
                && userName.length() <= maxNameLength
                && userName.matches(regex);
    }
}
