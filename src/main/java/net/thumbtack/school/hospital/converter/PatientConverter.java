package net.thumbtack.school.hospital.converter;

import net.thumbtack.school.hospital.dto.request.CreatePatientRequest;
import net.thumbtack.school.hospital.dto.response.BusyTicketsResponse;
import net.thumbtack.school.hospital.dto.response.CountOfBusyAppointmentsResponse;
import net.thumbtack.school.hospital.dto.response.PatientResponse;
import net.thumbtack.school.hospital.dto.response.TicketResponse;
import net.thumbtack.school.hospital.model.Patient;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PatientConverter {

    public Patient inflateEntity(CreatePatientRequest request) {
        return new Patient(request.getFirstName(), request.getLastName(), request.getPatronymic(), request.getLogin(), request.getPassword(), request.getEmail(), request.getAddress(), request.getPhone().replaceAll("-", ""));
    }

    public PatientResponse inflateResponse(Patient patient) {
        return new PatientResponse(patient.getId(), patient.getFirstName(), patient.getLastName(), patient.getPatronymic(), patient.getEmail(), patient.getAddress(), patient.getUserType(), patient.getPhone());
    }

    public CountOfBusyAppointmentsResponse inflateWithCountOfBusyAppointmentsResponse(Patient patient, int count) {
        return new CountOfBusyAppointmentsResponse(patient.getId(), patient.getFirstName(), patient.getLastName(), patient.getPatronymic(), patient.getEmail(), patient.getAddress(), patient.getPhone(), patient.getUserType(), count);
    }

    public BusyTicketsResponse inflateWithBusyTicketsResponse(Patient patient, List<TicketResponse> tickets) {
        return new BusyTicketsResponse(patient.getId(), patient.getFirstName(), patient.getLastName(), patient.getPatronymic(), patient.getEmail(), patient.getAddress(), patient.getPhone(), patient.getUserType(), tickets);
    }

}
