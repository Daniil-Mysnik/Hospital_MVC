package net.thumbtack.school.hospital.controller;

import net.thumbtack.school.hospital.dto.response.DoctorResponse;
import net.thumbtack.school.hospital.dto.response.PatientResponse;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/statistic")
public class StatisticController {
    private final StatisticService statisticService;

    @Autowired
    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping(value = "doctor/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public DoctorResponse getDoctor(@PathVariable(value = "id") int doctorId,
                                                     @RequestParam(name = "schedule", required = false, defaultValue = "no") String schedule,
                                                     @RequestParam(name = "dateStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateStart,
                                                     @RequestParam(name = "dateEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEnd) throws HospitalException {
        return statisticService.getDoctorWithFreeTickets(doctorId, schedule, dateStart, dateEnd);
    }

    @GetMapping(value = "doctors", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<DoctorResponse> getAllDoctors(@RequestParam(name = "schedule", required = false, defaultValue = "no") String schedule,
                                       @RequestParam(name = "dateStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateStart,
                                       @RequestParam(name = "dateEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEnd) throws HospitalException {
        return statisticService.getAllDoctorsWithFreeTickets(schedule, dateStart, dateEnd);
    }

    @GetMapping(value = "patient/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public PatientResponse getPatient(@CookieValue("JAVASESSIONID") String sessionId, @PathVariable int id,
                                      @RequestParam(name = "ticket", required = false, defaultValue = "no") String tickets,
                                      @RequestParam(name = "dateStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateStart,
                                      @RequestParam(name = "dateEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEnd) throws HospitalException {
        return statisticService.getPatientWithHisAppointments(sessionId, id, tickets, dateStart, dateEnd);
    }

}
