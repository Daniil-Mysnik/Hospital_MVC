package net.thumbtack.school.hospital.controller;

import net.thumbtack.school.hospital.dto.response.LoginResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class TestController {

    @GetMapping("main")
    public String get(Model model, HttpServletRequest request) {
        LoginResponse response = (LoginResponse) request.getSession().getAttribute("login");
        model.addAttribute("login", response);
        return "main";
    }
}
