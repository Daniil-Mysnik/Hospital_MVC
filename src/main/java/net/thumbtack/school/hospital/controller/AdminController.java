package net.thumbtack.school.hospital.controller;

import net.thumbtack.school.hospital.dto.request.CreateAdminRequest;
import net.thumbtack.school.hospital.dto.request.UpdateAdminRequest;
import net.thumbtack.school.hospital.dto.response.AdminResponse;
import net.thumbtack.school.hospital.dto.response.LoginResponse;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("adminRegister")
    public String create(HttpServletRequest request) {
        LoginResponse loginResponse = (LoginResponse) request.getSession().getAttribute("login");
        if (loginResponse == null || !loginResponse.getUserResponse().getUserType().equals(UserType.ADMIN)) {
            return "403";
        }
        return "registerAdmin";
    }

    @PostMapping("adminRegister")
    public String create(@Valid CreateAdminRequest createRq, HttpServletRequest request) throws HospitalException {
        LoginResponse loginResponse = (LoginResponse) request.getSession().getAttribute("login");
        if (loginResponse == null || !loginResponse.getUserResponse().getUserType().equals(UserType.ADMIN)) {
            return "403";
        }
        adminService.create(loginResponse.getSessionId(), createRq);
        return "redirect:/main";
    }

    @PostMapping("adminUpdate")
    public String update(@Valid UpdateAdminRequest updateRq, HttpServletRequest request) throws HospitalException {
        LoginResponse loginResponse = (LoginResponse) request.getSession().getAttribute("login");
        if (loginResponse == null || !loginResponse.getUserResponse().getUserType().equals(UserType.ADMIN)) {
            return "403";
        }
        AdminResponse adminRs = adminService.update(loginResponse.getSessionId(), updateRq);
        loginResponse.setUserResponse(adminRs);
        return "redirect:/account";
    }

}
