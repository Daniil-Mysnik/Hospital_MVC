package net.thumbtack.school.hospital.dto.response;

import java.util.List;

public class TicketListResponse {
    List<TicketResponse> tickets;

    public TicketListResponse() {
    }

    public TicketListResponse(List<TicketResponse> tickets) {
        this.tickets = tickets;
    }

    public List<TicketResponse> getTickets() {
        return tickets;
    }

    public void setTickets(List<TicketResponse> tickets) {
        this.tickets = tickets;
    }

}
