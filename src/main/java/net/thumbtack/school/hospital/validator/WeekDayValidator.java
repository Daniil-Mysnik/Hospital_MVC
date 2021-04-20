package net.thumbtack.school.hospital.validator;

import net.thumbtack.school.hospital.dto.request.AppointmentRequest;
import net.thumbtack.school.hospital.dto.request.ScheduleRequest;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

@Component
public class WeekDayValidator implements ConstraintValidator<WeekDay, ScheduleRequest> {
    @Override
    public void initialize(WeekDay constraintAnnotation) {
    }

    @Override
    public boolean isValid(ScheduleRequest scheduleRequest, ConstraintValidatorContext constraintValidatorContext) {
        if (scheduleRequest.getWeekDaysSchedule() != null) {
            List<AppointmentRequest> appointmentRequests = scheduleRequest.getWeekDaysSchedule().getDaySchedule();
            for (AppointmentRequest appointmentRequest : appointmentRequests) {
                String weekDay = appointmentRequest.getWeekDay();
                if (!checkEquals(weekDay)) {
                    return false;
                }
            }
        }
        if (scheduleRequest.getWeekSchedule() != null) {
            for (String weekDay : scheduleRequest.getWeekSchedule().getWeekDays()) {
                if (!checkEquals(weekDay)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkEquals(String weekDay) {
        return weekDay.equalsIgnoreCase("MON") ||
                weekDay.equalsIgnoreCase("TUE") ||
                weekDay.equalsIgnoreCase("WED") ||
                weekDay.equalsIgnoreCase("THU") ||
                weekDay.equalsIgnoreCase("FRI");
    }

}
