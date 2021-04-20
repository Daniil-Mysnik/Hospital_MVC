package net.thumbtack.school.hospital.controller;

import net.thumbtack.school.hospital.dto.request.CreateAdminRequest;
import net.thumbtack.school.hospital.dto.request.UpdateAdminRequest;
import net.thumbtack.school.hospital.dto.response.AdminResponse;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/admins")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AdminResponse create(@CookieValue("JAVASESSIONID") String sessionId,
                                @Valid @RequestBody CreateAdminRequest request) throws HospitalException {
        return adminService.create(sessionId, request);
    }

    @PutMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public AdminResponse update(@CookieValue("JAVASESSIONID") String sessionId,
                                @Valid @RequestBody UpdateAdminRequest request) throws HospitalException {
        return adminService.update(sessionId, request);
    }

}
