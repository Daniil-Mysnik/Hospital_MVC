package net.thumbtack.school.hospital.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public class TicketResponse {
    private String ticket;
    private String room;
    private LocalDate date;
    private LocalTime time;


    public TicketResponse() {
    }

    public TicketResponse(String ticket, String room, LocalDate date, LocalTime time) {
        this.ticket = ticket;
        this.room = room;
        this.date = date;
        this.time = time;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

}
