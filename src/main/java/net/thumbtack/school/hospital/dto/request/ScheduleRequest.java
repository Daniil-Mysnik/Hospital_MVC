package net.thumbtack.school.hospital.dto.request;

import net.thumbtack.school.hospital.validator.Date;
import net.thumbtack.school.hospital.validator.OneSchedule;
import net.thumbtack.school.hospital.validator.Time;
import net.thumbtack.school.hospital.validator.WeekDay;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.LocalDate;

@Date
@OneSchedule
@Time
@WeekDay
public class ScheduleRequest {
    private LocalDate dateStart;
    private LocalDate dateEnd;
    private WeekScheduleRequest weekSchedule;
    private WeekDaysScheduleRequest weekDaysSchedule;

    @Min(value = 3, message = "minimum duration 3 min")
    @Max(value = 59, message = "maximum duration 59 min")
    private int duration;

    public ScheduleRequest() {
    }

    public ScheduleRequest(LocalDate dateStart, LocalDate dateEnd, WeekScheduleRequest weekSchedule, WeekDaysScheduleRequest weekDaysSchedule, int duration) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.weekSchedule = weekSchedule;
        this.weekDaysSchedule = weekDaysSchedule;
        this.duration = duration;
    }

    public LocalDate getDateStart() {
        return dateStart;
    }

    public void setDateStart(LocalDate dateStart) {
        this.dateStart = dateStart;
    }

    public LocalDate getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(LocalDate dateEnd) {
        this.dateEnd = dateEnd;
    }

    public WeekScheduleRequest getWeekSchedule() {
        return weekSchedule;
    }

    public void setWeekSchedule(WeekScheduleRequest weekSchedule) {
        this.weekSchedule = weekSchedule;
    }

    public WeekDaysScheduleRequest getWeekDaysSchedule() {
        return weekDaysSchedule;
    }

    public void setWeekDaysSchedule(WeekDaysScheduleRequest weekDaysSchedule) {
        this.weekDaysSchedule = weekDaysSchedule;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}

