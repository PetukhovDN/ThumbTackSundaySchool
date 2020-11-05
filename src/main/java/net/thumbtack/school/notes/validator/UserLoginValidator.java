package net.thumbtack.school.notes.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserLoginValidator  implements ConstraintValidator<UserLogin, String> {
    @Autowired
    private Environment env;

    @Value("${max_name_length}")
    private long max_name_length;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return true;
    }
}
