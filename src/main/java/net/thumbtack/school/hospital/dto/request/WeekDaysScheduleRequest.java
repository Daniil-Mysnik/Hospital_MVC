package net.thumbtack.school.hospital.dto.request;

import java.util.List;

public class WeekDaysScheduleRequest {
    private List<AppointmentRequest> daySchedule;

    public WeekDaysScheduleRequest() {
    }

    public WeekDaysScheduleRequest(List<AppointmentRequest> daySchedule) {
        this.daySchedule = daySchedule;
    }

    public List<AppointmentRequest> getDaySchedule() {
        return daySchedule;
    }

    public void setDaySchedule(List<AppointmentRequest> daySchedule) {
        this.daySchedule = daySchedule;
    }

}
