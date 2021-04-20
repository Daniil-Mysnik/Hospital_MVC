package net.thumbtack.school.hospital.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CommissionResponse {
    private String ticket;
    private int patientId;
    private List<Integer> doctorIds;
    private String room;
    private LocalDate date;
    private LocalTime time;
    private int duration;

    public CommissionResponse() {
    }

    public CommissionResponse(String ticket, int patientId, List<Integer> doctorIds, String room, LocalDate date, LocalTime time, int duration) {
        this.ticket = ticket;
        this.patientId = patientId;
        this.doctorIds = doctorIds;
        this.room = room;
        this.date = date;
        this.time = time;
        this.duration = duration;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public List<Integer> getDoctorIds() {
        return doctorIds;
    }

    public void setDoctorIds(List<Integer> doctorIds) {
        this.doctorIds = doctorIds;
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

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

}
