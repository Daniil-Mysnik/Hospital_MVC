package net.thumbtack.school.hospital.validator;

import net.thumbtack.school.hospital.dto.request.AppointmentRequest;
import net.thumbtack.school.hospital.dto.request.ScheduleRequest;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalTime;

@Component
public class TimeValidator implements ConstraintValidator<Time, ScheduleRequest> {
    @Override
    public void initialize(Time constraintAnnotation) {
    }

    @Override
    public boolean isValid(ScheduleRequest scheduleRequest, ConstraintValidatorContext constraintValidatorContext) {
        if(scheduleRequest.getWeekSchedule() != null) {
            if(!checkTime(scheduleRequest.getWeekSchedule().getTimeStart(), scheduleRequest.getWeekSchedule().getTimeEnd())) {
                return false;
            }
        }
        if (scheduleRequest.getWeekDaysSchedule() != null) {
            for (AppointmentRequest appointmentRequest : scheduleRequest.getWeekDaysSchedule().getDaySchedule()) {
                if (!checkTime(appointmentRequest.getTimeStart(), appointmentRequest.getTimeEnd())) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkTime(LocalTime timeStart, LocalTime timeEnd) {
        return timeStart != null &&
                timeEnd != null &&
                !timeEnd.minusHours(8).isAfter(timeStart);
    }

}
