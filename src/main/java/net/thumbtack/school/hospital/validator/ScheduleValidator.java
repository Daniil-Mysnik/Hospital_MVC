package net.thumbtack.school.hospital.validator;

import net.thumbtack.school.hospital.dto.request.ScheduleRequest;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Component
public class ScheduleValidator implements ConstraintValidator<OneSchedule, ScheduleRequest> {
    @Override
    public void initialize(OneSchedule constraintAnnotation) {
    }

    @Override
    public boolean isValid(ScheduleRequest scheduleRequest, ConstraintValidatorContext constraintValidatorContext) {
        return (scheduleRequest.getWeekSchedule() != null && scheduleRequest.getWeekDaysSchedule() == null) ||
                (scheduleRequest.getWeekSchedule() == null && scheduleRequest.getWeekDaysSchedule() != null);
    }

}
