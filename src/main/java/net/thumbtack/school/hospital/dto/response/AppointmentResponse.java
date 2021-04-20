package net.thumbtack.school.hospital.dto.response;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class AppointmentResponse {
    LocalTime time;

    public AppointmentResponse() {
    }

    public AppointmentResponse(LocalTime time) {
        this.time = time.truncatedTo(ChronoUnit.MINUTES);
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

}
