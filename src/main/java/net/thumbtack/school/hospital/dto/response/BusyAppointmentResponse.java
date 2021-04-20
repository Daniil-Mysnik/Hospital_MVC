package net.thumbtack.school.hospital.dto.response;

import java.time.LocalTime;

public class BusyAppointmentResponse extends AppointmentResponse {
    PatientResponse patient;

    public BusyAppointmentResponse(LocalTime time, PatientResponse patient) {
        super(time);
        this.patient = patient;
    }

    public PatientResponse getPatient() {
        return patient;
    }

    public void setPatient(PatientResponse patient) {
        this.patient = patient;
    }

}
