package net.thumbtack.school.hospital.controller;

import net.thumbtack.school.hospital.dto.request.CommissionRequest;
import net.thumbtack.school.hospital.dto.response.CommissionResponse;
import net.thumbtack.school.hospital.dto.response.EmptyResponse;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.service.CommissionService;
import net.thumbtack.school.hospital.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("api/commissions")
public class CommissionController {
    private final CommissionService commissionService;
    private final TicketService ticketService;

    @Autowired
    public CommissionController(CommissionService commissionService, TicketService ticketService) {
        this.commissionService = commissionService;
        this.ticketService = ticketService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommissionResponse create(@CookieValue("JAVASESSIONID") String sessionId,
                                     @Valid @RequestBody CommissionRequest request) throws HospitalException {
        return commissionService.create(sessionId, request);
    }

    @DeleteMapping(value = "{ticket}", produces = MediaType.APPLICATION_JSON_VALUE)
    public EmptyResponse delete(@CookieValue("JAVASESSIONID") String sessionId, @PathVariable String ticket) throws HospitalException {
        return ticketService.delete(sessionId, ticket);
    }

}
