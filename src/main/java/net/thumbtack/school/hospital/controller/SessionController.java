package net.thumbtack.school.hospital.controller;

import net.thumbtack.school.hospital.dto.request.LoginRequest;
import net.thumbtack.school.hospital.dto.response.EmptyResponse;
import net.thumbtack.school.hospital.dto.response.LoginResponse;
import net.thumbtack.school.hospital.dto.response.UserResponse;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class SessionController {
    private final SessionService sessionService;

    @Autowired
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("login")
    public String login(@RequestParam String login, @RequestParam String password, HttpServletRequest request) throws HospitalException {
        LoginRequest loginRequest = new LoginRequest(login, password);
        LoginResponse loginResponse = sessionService.create(loginRequest);
        HttpSession session = request.getSession();
        session.setAttribute("login", loginResponse);
        return "redirect:/main";
    }

    @GetMapping("login")
    public String login() {
        return "login";
    }

    @PostMapping("logout")
    public String logout(@RequestParam String sessionId, HttpServletRequest request) throws HospitalException {
        sessionService.delete(sessionId);
        request.getSession().removeAttribute("login");
        return "redirect:/main";
    }
}
