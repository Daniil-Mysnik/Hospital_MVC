package net.thumbtack.school.hospital.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

public class UpdateScheduleRequest extends ScheduleRequest {

    public UpdateScheduleRequest() {
    }

    public UpdateScheduleRequest(LocalDate dateStart, LocalDate dateEnd, WeekScheduleRequest weekSchedule, WeekDaysScheduleRequest weekDaysSchedule, int duration) {
        super(dateStart, dateEnd, weekSchedule, weekDaysSchedule, duration);
    }

}
