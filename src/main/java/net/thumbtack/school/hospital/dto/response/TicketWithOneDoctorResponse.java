package net.thumbtack.school.hospital.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

public class TicketWithOneDoctorResponse extends TicketResponse {
    private int doctorId;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String speciality;

    public TicketWithOneDoctorResponse() {
    }

    public TicketWithOneDoctorResponse(String ticket, String room, LocalDate date, LocalTime time, int doctorId, String firstName, String lastName, String patronymic, String speciality) {
        super(ticket, room, date, time);
        this.doctorId = doctorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.speciality = speciality;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

}
