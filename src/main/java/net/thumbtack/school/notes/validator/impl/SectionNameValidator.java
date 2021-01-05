package net.thumbtack.school.notes.validator.impl;

import net.thumbtack.school.notes.validator.SectionName;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Section name validation in creating section method.
 * Cannot be null or empty.
 * In section name can be used letters of the Russian and English alphabets, numbers and space and dash characters.
 */
public class SectionNameValidator implements ConstraintValidator<SectionName, String> {
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        String regex = "^[a-zA-Zа-яА-ЯёЁ0-9\\s.'-]+$";
        return s != null
                && !s.isBlank()
                && s.matches(regex);
    }
}
