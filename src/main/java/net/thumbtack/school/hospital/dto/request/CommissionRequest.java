package net.thumbtack.school.hospital.dto.request;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class CommissionRequest {
    private int patientId;

    @NotNull.List(@NotNull(message = "Commission cannot be without doctors"))
    private List<Integer> doctorIds;

    @NotNull(message = "Room number required!")
    @NotBlank(message = "Room number required!")
    private String room;

    @Future(message = "Date must be later than current")
    private LocalDate date;

    private LocalTime time;

    @Min(value = 1, message = "Minimum time 3 min")
    @Max(value = 59, message = "Maximum time 59 min")
    private int duration;

    public CommissionRequest() {
    }

    public CommissionRequest(int patientId, List<Integer> doctorIds, String room, LocalDate date, LocalTime time, int duration) {
        this.patientId = patientId;
        this.doctorIds = doctorIds;
        this.room = room;
        this.date = date;
        this.time = time;
        this.duration = duration;
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
