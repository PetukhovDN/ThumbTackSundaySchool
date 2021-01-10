package net.thumbtack.school.notes.validator.impl;

import lombok.SneakyThrows;
import net.thumbtack.school.notes.exceptions.ExceptionErrorInfo;
import net.thumbtack.school.notes.exceptions.NoteServerException;
import net.thumbtack.school.notes.validator.Rating;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Rating number validation
 */
public class RatingValidator implements ConstraintValidator<Rating, String> {
    /**
     * Rating must be a number between one and five
     */
    @SneakyThrows
    @Override
    public boolean isValid(String ratingString, ConstraintValidatorContext constraintValidatorContext) {
        try {
            int rating = Integer.parseInt(ratingString);
            return rating >= 1 && rating <= 5;
        } catch (NumberFormatException exc) {
            throw new NoteServerException(ExceptionErrorInfo.INCORRECT_RATING_FORMAT, exc.getMessage());
        }
    }
}
