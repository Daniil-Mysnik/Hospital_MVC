package net.thumbtack.school.hospital.dto.response;


import java.time.LocalTime;

public class FreeAppointmentResponse extends AppointmentResponse {

    public FreeAppointmentResponse(LocalTime time) {
        super(time);
    }

}
