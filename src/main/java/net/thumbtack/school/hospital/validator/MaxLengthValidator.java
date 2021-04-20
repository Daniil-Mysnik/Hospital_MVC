package net.thumbtack.school.hospital.validator;

import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MaxLengthValidator implements ConstraintValidator<MaxLength, String> {
    @Value("${max_name_length}")
    private int maxLength;

    @Override
    public void initialize(MaxLength constraintAnnotation) {
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s == null || s.length() <= maxLength;
    }

}
