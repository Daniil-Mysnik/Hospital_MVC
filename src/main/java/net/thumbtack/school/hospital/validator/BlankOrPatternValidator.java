package net.thumbtack.school.hospital.validator;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlankOrPatternValidator implements ConstraintValidator<BlankOrPattern, String> {
    @Value("${max_name_length}")
    private int maxLength;
    private Pattern pattern;

    public void initialize(BlankOrPattern parameters) {
        this.pattern = Pattern.compile(parameters.regexp());
    }

    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null || value.length() == 0) {
            return true;
        }
        Matcher m = pattern.matcher(value);
        return m.matches() && value.length() <= maxLength;
    }

}
