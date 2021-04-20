package net.thumbtack.school.hospital.dto.request;

import net.thumbtack.school.hospital.validator.Time;

import java.time.LocalTime;
import java.util.List;

@Time
public class WeekScheduleRequest {
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private List<String> weekDays;

    public WeekScheduleRequest() {
    }

    public WeekScheduleRequest(LocalTime timeStart, LocalTime timeEnd, List<String> weekDays) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.weekDays = weekDays;
    }

    public LocalTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public List<String> getWeekDays() {
        return weekDays;
    }

    public void setWeekDays(List<String> weekDays) {
        this.weekDays = weekDays;
    }

}
