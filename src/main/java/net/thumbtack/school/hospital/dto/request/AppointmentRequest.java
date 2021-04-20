package net.thumbtack.school.hospital.dto.request;

import java.time.LocalTime;

public class AppointmentRequest {
    private String weekDay;
    private LocalTime timeStart;
    private LocalTime timeEnd;

    public AppointmentRequest() {
    }

    public AppointmentRequest(String weekDay, LocalTime timeStart, LocalTime timeEnd) {
        this.weekDay = weekDay;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public void setWeekDay(String weekDay) {
        this.weekDay = weekDay;
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

}
