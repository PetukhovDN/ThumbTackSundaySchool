package net.thumbtack.school.notes.validator.impl;

import net.thumbtack.school.notes.validator.UserPatronymic;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserPatronymicValidator implements ConstraintValidator<UserPatronymic, String> {

    private final String regex = "^[a-zA-Zа-яА-ЯёЁ\\s.'-]+$";
    @Value("${max_name_length}")
    private long max_name_length;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null
                && s.length() <= max_name_length
                && (s.matches(regex) || s.isBlank());
    }
}
