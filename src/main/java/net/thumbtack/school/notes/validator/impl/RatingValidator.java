package net.thumbtack.school.notes.validator.impl;

import net.thumbtack.school.notes.validator.Rating;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Rating number validation
 */
public class RatingValidator implements ConstraintValidator<Rating, Integer> {
    /**
     * Rating must be between one and five numbers
     */
    @Override
    public boolean isValid(Integer s, ConstraintValidatorContext constraintValidatorContext) {
        return s >= 1 && s <= 5;
    }
}
