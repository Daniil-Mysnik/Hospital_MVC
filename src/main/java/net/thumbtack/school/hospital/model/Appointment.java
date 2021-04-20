package net.thumbtack.school.hospital.model;

import java.time.LocalTime;
import java.util.Objects;

public class Appointment {
    private int id;
    private DaySchedule daySchedule;
    private LocalTime time;
    private int duration;
    private Patient patient;
    private String ticket;
    private State state;

    public Appointment() {
    }

    public Appointment(int id, DaySchedule daySchedule, LocalTime time, int duration, Patient patient, String ticket, State state) {
        this.id = id;
        this.daySchedule = daySchedule;
        this.time = time;
        this.duration = duration;
        this.patient = patient;
        this.ticket = ticket;
        this.state = state;
    }

    public Appointment(LocalTime time, int duration, Patient patient, String ticket, State state) {
        this(0, null, time, duration, patient, ticket, state);
    }

    public Appointment(LocalTime time, int duration, State state) {
        this(0, null, time, duration, null, null, state);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public DaySchedule getDaySchedule() {
        return daySchedule;
    }

    public void setDaySchedule(DaySchedule daySchedule) {
        this.daySchedule = daySchedule;
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

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return id == that.id &&
                duration == that.duration &&
                Objects.equals(daySchedule, that.daySchedule) &&
                Objects.equals(time, that.time) &&
                Objects.equals(patient, that.patient) &&
                Objects.equals(ticket, that.ticket) &&
                state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, daySchedule, time, duration, patient, ticket, state);
    }
}
