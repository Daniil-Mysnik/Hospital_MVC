package net.thumbtack.school.hospital.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TicketCommissionResponse extends TicketResponse {
    List<DoctorWithoutRoomResponse> doctors;

    public TicketCommissionResponse(String ticket, String room, LocalDate date, LocalTime time, List<DoctorWithoutRoomResponse> doctors) {
        super(ticket, room, date, time);
        this.doctors = doctors;
    }

    public List<DoctorWithoutRoomResponse> getDoctors() {
        return doctors;
    }

    public void setDoctors(List<DoctorWithoutRoomResponse> doctors) {
        this.doctors = doctors;
    }

}
