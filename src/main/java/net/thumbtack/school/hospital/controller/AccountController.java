package net.thumbtack.school.hospital.controller;

import net.thumbtack.school.hospital.dto.response.AdminResponse;
import net.thumbtack.school.hospital.dto.response.DoctorResponse;
import net.thumbtack.school.hospital.dto.response.LoginResponse;
import net.thumbtack.school.hospital.dto.response.PatientResponse;
import net.thumbtack.school.hospital.exceptions.HospitalErrorCode;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AccountController {
    private final SessionService sessionService;

    @Autowired
    public AccountController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("account")
    public String get(HttpServletRequest request, Model model) throws HospitalException {
        LoginResponse response = (LoginResponse) request.getSession().getAttribute("login");

        switch (response.getUserResponse().getUserType()) {
            case ADMIN:
                model.addAttribute("account", (AdminResponse) sessionService.getInfo(response.getSessionId()));
                break;
            case DOCTOR:
                model.addAttribute("account", (DoctorResponse) sessionService.getInfo(response.getSessionId()));
                break;
            case PATIENT:
                model.addAttribute("account", (PatientResponse) sessionService.getInfo(response.getSessionId()));
                break;
            default: throw new HospitalException(HospitalErrorCode.UNKNOWN_USER_TYPE);
        }

        return "account";
    }

}
