package net.thumbtack.school.hospital.controller;

import net.thumbtack.school.hospital.dto.request.CreatePatientRequest;
import net.thumbtack.school.hospital.dto.request.LoginRequest;
import net.thumbtack.school.hospital.dto.request.UpdatePatientRequest;
import net.thumbtack.school.hospital.dto.response.LoginResponse;
import net.thumbtack.school.hospital.dto.response.PatientResponse;
import net.thumbtack.school.hospital.dto.response.UserResponse;
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
    public String register(HttpServletRequest request) {
        LoginResponse loginResponse = (LoginResponse) request.getSession().getAttribute("login");
        if (loginResponse != null && loginResponse.getUserResponse().getUserType().equals(UserType.PATIENT)) {
            return "403";
        }
        return "registerPatient";
    }

    @PostMapping("patientRegister")
    public String register(@Valid CreatePatientRequest createRq, HttpServletRequest request) throws HospitalException {
        LoginResponse loginResponse = (LoginResponse) request.getSession().getAttribute("login");
        if (loginResponse != null && loginResponse.getUserResponse().getUserType().equals(UserType.PATIENT)) {
            return "403";
        }
        patientService.create(createRq);
        loginResponse = sessionService.create(new LoginRequest(createRq.getLogin(), createRq.getPassword()));
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

    @PostMapping("patientUpdate")
    public String update(@Valid UpdatePatientRequest updateRq, HttpServletRequest request) throws HospitalException {
        LoginResponse loginResponse = (LoginResponse) request.getSession().getAttribute("login");
        if (loginResponse == null) {
            return "403";
        }
        PatientResponse patientRs = patientService.update(loginResponse.getSessionId(), updateRq);
        loginResponse.setUserResponse(patientRs);
        return "redirect:/account";
    }

}
