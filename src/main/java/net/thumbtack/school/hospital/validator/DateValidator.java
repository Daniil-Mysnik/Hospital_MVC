package net.thumbtack.school.hospital.validator;

import net.thumbtack.school.hospital.dto.request.ScheduleRequest;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

@Component
public class DateValidator implements ConstraintValidator<Date, ScheduleRequest> {
    @Override
    public void initialize(Date constraintAnnotation) {
    }

    @Override
    public boolean isValid(ScheduleRequest scheduleRequest, ConstraintValidatorContext constraintValidatorContext) {
        return scheduleRequest.getDateStart() != null &&
                scheduleRequest.getDateEnd() != null &&
                scheduleRequest.getDateStart().isAfter(LocalDate.now()) && scheduleRequest.getDateEnd().isAfter(scheduleRequest.getDateStart());
    }
}
