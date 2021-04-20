package net.thumbtack.school.hospital.controller;

import net.thumbtack.school.hospital.dto.request.CreatePatientRequest;
import net.thumbtack.school.hospital.dto.request.LoginRequest;
import net.thumbtack.school.hospital.dto.request.UpdatePatientRequest;
import net.thumbtack.school.hospital.dto.response.LoginResponse;
import net.thumbtack.school.hospital.dto.response.PatientResponse;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.service.PatientService;
import net.thumbtack.school.hospital.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class PatientController {
    private final PatientService patientService;
    private final SessionService sessionService;

    @Autowired
    public PatientController(PatientService patientService, SessionService sessionService) {
        this.patientService = patientService;
        this.sessionService = sessionService;
    }

    @GetMapping("patientRegister")
    public String register() {
        return "registerPatient";
    }

    @PostMapping("patientRegister")
    public String register(@RequestParam String firstName,
                           @RequestParam String lastName,
                           @RequestParam String patronymic,
                           @RequestParam String email,
                           @RequestParam String address,
                           @RequestParam String phone,
                           @RequestParam String login,
                           @RequestParam String password,
                           HttpServletRequest request) throws HospitalException {
        CreatePatientRequest createPatientRequest = new CreatePatientRequest(firstName,
                                                                             lastName,
                                                                             patronymic,
                                                                             login, password,
                                                                             email,
                                                                             address,
                                                                             phone);
        patientService.create(createPatientRequest);
        LoginResponse loginResponse = sessionService.create(new LoginRequest(createPatientRequest.getLogin(), createPatientRequest.getPassword()));
        HttpSession session = request.getSession();
        if (session.getAttribute("login") == null) {
            session.setAttribute("login", loginResponse);
        }
        return "redirect:/main";
    }


//    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
//    public PatientResponse get(@CookieValue("JAVASESSIONID") String sessionId, @PathVariable(value = "id") int patientId) throws HospitalException {
//        return patientService.get(sessionId, patientId);
//    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public PatientResponse update(@CookieValue("JAVASESSIONID") String sessionId, @Valid @RequestBody UpdatePatientRequest request) throws HospitalException {
        return patientService.update(sessionId, request);
    }

}
