package net.thumbtack.school.hospital.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class DaySchedule implements Comparable<DaySchedule> {
    private int id;
    private Doctor doctor;
    private LocalDate date;
    private LocalTime timeStart;
    private LocalTime timeEnd;
    private List<Appointment> appointments;

    public DaySchedule() {
    }

    public DaySchedule(int id, LocalDate date, LocalTime timeStart, LocalTime timeEnd, List<Appointment> appointments) {
        this.id = id;
        this.date = date;
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.appointments = appointments;
    }

    public DaySchedule(LocalDate date, LocalTime timeStart, LocalTime timeEnd, List<Appointment> appointments) {
        this(0, date, timeStart, timeEnd, appointments);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(LocalTime timeStart) {
        this.timeStart = timeStart;
    }

    public LocalTime getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(LocalTime timeEnd) {
        this.timeEnd = timeEnd;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(List<Appointment> appointments) {
        this.appointments = appointments;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DaySchedule that = (DaySchedule) o;
        return id == that.id &&
                Objects.equals(doctor, that.doctor) &&
                Objects.equals(date, that.date) &&
                Objects.equals(timeStart, that.timeStart) &&
                Objects.equals(timeEnd, that.timeEnd) &&
                Objects.equals(appointments, that.appointments);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, doctor, date, timeStart, timeEnd, appointments);
    }

    @Override
    public int compareTo(DaySchedule daySchedule) {
        return this.date.compareTo(daySchedule.getDate());
    }

}
