package net.thumbtack.school.hospital.converter;

import net.thumbtack.school.hospital.dto.request.TicketRequest;
import net.thumbtack.school.hospital.dto.response.DoctorResponse;
import net.thumbtack.school.hospital.dto.response.TicketResponse;
import net.thumbtack.school.hospital.dto.response.TicketWithOneDoctorResponse;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Ticket;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TicketConverter {

    public TicketWithOneDoctorResponse inflateResponse(String ticketNumber, DoctorResponse doctorResponse, TicketRequest request) {
        return new TicketWithOneDoctorResponse(ticketNumber, doctorResponse.getRoom(), request.getDate(),
                request.getTime(), doctorResponse.getId(), doctorResponse.getFirstName(), doctorResponse.getLastName(), doctorResponse.getPatronymic(), doctorResponse.getSpeciality());
    }

    public List<TicketResponse> inflateResponses(List<Ticket> tickets) {
        List<TicketResponse> responses = new ArrayList<>();
        for (Ticket ticket : tickets) {
            responses.add(new TicketResponse(ticket.getNumber(), ticket.getRoom(), ticket.getDate(), ticket.getTime()));
        }
        return responses;
    }

}
