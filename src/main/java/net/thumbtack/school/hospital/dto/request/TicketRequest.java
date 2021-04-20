package net.thumbtack.school.hospital.dto.request;

import net.thumbtack.school.hospital.validator.IdOrSpeciality;

import javax.validation.constraints.Future;
import java.time.LocalDate;
import java.time.LocalTime;

@IdOrSpeciality
public class TicketRequest {
    private int doctorId;
    private String speciality;
    @Future(message = "Date must be later than current")
    private LocalDate date;
    private LocalTime time;

    public TicketRequest() {
    }

    public TicketRequest(int doctorId, String speciality, LocalDate date, LocalTime time) {
        this.doctorId = doctorId;
        this.speciality = speciality;
        this.date = date;
        this.time = time;
    }

    public TicketRequest(int doctorId, LocalDate date, LocalTime time) {
        this(doctorId, null, date, time);
    }

    public TicketRequest(String speciality, LocalDate date, LocalTime time) {
        this(0, speciality, date, time);
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
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
