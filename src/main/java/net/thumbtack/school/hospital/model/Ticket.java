package net.thumbtack.school.hospital.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Ticket {
    private int id;
    private String number;
    private String room;
    private Patient patient;
    private int doctorId;
    private List<Integer> doctorIds;
    private LocalDate date;
    private LocalTime time;
    private int duration;

    public Ticket() {
    }

    public Ticket(int id) {
        this.id = id;
    }

    public Ticket(String number) {
        this.number = number;
    }

    public Ticket(int doctorId, LocalDate date, LocalTime time) {
        this.doctorId = doctorId;
        this.date = date;
        this.time = time;
        this.number = "D" + doctorId + date.format(DateTimeFormatter.BASIC_ISO_DATE) + time.format(DateTimeFormatter.ofPattern("HHmm"));
    }

    public Ticket(List<Integer> doctorIds, LocalDate date, LocalTime time) {
        this.doctorIds = doctorIds;
        this.date = date;
        this.time = time;
        String doctors = doctorIds.stream().map(id -> "D" + id).collect(Collectors.joining());
        this.number = "C" + doctors + date.format(DateTimeFormatter.BASIC_ISO_DATE) + time.format(DateTimeFormatter.ofPattern("HHmm"));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public List<Integer> getDoctorIds() {
        return doctorIds;
    }

    public void setDoctorIds(List<Integer> doctorIds) {
        this.doctorIds = doctorIds;
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

    public List<Integer> getDoctorsFromTicket(String ticket) {
        ticket = ticket.substring(0, ticket.length() - 12);
        List<Integer> result = new ArrayList<>();
        String[] nums = ticket.split("D");
        for (int i = 1; i < nums.length; i++)
            result.add(Integer.valueOf(nums[i]));
        return result;
    }

    @Override
    public String toString() {
        return number;
    }

}
