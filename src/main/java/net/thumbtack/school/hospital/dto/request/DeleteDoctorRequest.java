package net.thumbtack.school.hospital.dto.request;

import javax.validation.constraints.Future;
import java.time.LocalDate;

public class DeleteDoctorRequest {
    @Future(message = "The date of dismissal cannot be earlier than the current")
    LocalDate date;

    public DeleteDoctorRequest() {
    }

    public DeleteDoctorRequest(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
