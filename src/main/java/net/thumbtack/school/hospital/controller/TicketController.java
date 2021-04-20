package net.thumbtack.school.hospital.controller;

import net.thumbtack.school.hospital.dto.request.TicketRequest;
import net.thumbtack.school.hospital.dto.response.EmptyResponse;
import net.thumbtack.school.hospital.dto.response.LoginResponse;
import net.thumbtack.school.hospital.dto.response.TicketListResponse;
import net.thumbtack.school.hospital.dto.response.TicketResponse;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@Controller
public class TicketController {
    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public TicketResponse create(@CookieValue("JAVASESSIONID") String sessionId,
                                 @Valid @RequestBody TicketRequest request) throws HospitalException {
        return ticketService.create(sessionId, request);
    }

//    @GetMapping("tickets")
//    public String getTicketList(Model model, HttpServletRequest request) throws HospitalException {
//        LoginResponse loginResponse = (LoginResponse) request.getSession().getAttribute("login");
//        model.addAttribute("appointmentList", ticketService.getTicketList(loginResponse.getSessionId()).getTickets());
//        return "tickets";
//    }

    @DeleteMapping(value = "{ticketNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponse delete(@CookieValue("JAVASESSIONID") String sessionId, @PathVariable String ticketNumber) throws HospitalException {
        return ticketService.delete(sessionId, ticketNumber);
    }

}
