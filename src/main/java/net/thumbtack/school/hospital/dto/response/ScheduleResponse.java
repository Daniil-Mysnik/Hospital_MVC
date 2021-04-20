package net.thumbtack.school.hospital.dto.response;

import java.time.LocalDate;
import java.util.List;

public class ScheduleResponse {
    private LocalDate date;
    private List<AppointmentResponse> daySchedule;

    public ScheduleResponse() {
    }

    public ScheduleResponse(LocalDate date, List<AppointmentResponse> daySchedule) {
        this.date = date;
        this.daySchedule = daySchedule;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<AppointmentResponse> getDaySchedule() {
        return daySchedule;
    }

    public void setDaySchedule(List<AppointmentResponse> daySchedule) {
        this.daySchedule = daySchedule;
    }

}
