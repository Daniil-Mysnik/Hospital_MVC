package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.converter.AdminConverter;
import net.thumbtack.school.hospital.dao.AdminDAO;
import net.thumbtack.school.hospital.dao.UserDAO;
import net.thumbtack.school.hospital.dto.request.CreateAdminRequest;
import net.thumbtack.school.hospital.dto.request.UpdateAdminRequest;
import net.thumbtack.school.hospital.dto.response.AdminResponse;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.User;
import net.thumbtack.school.hospital.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    private final UserDAO userDAO;
    private final AdminDAO adminDAO;
    private final AdminConverter adminConverter;
    private final UserValidator userValidator;

    @Autowired
    public AdminService(UserDAO userDAO,
                        AdminDAO adminDAO,
                        AdminConverter adminConverter,
                        UserValidator userValidator) {
        this.userDAO = userDAO;
        this.adminDAO = adminDAO;
        this.adminConverter = adminConverter;
        this.userValidator = userValidator;
    }

    public AdminResponse create(String sessionId, CreateAdminRequest request) throws HospitalException {
        userValidator.checkUserInSessionAdmin(sessionId);
        Admin admin = adminDAO.save(adminConverter.inflateEntity(request));
        return adminConverter.inflateResponse(admin);
    }

    public AdminResponse update(String sessionId, UpdateAdminRequest request) throws HospitalException {
        User user = userDAO.getBySessionId(sessionId);
        userValidator.checkPasswordMatch(user.getPassword(), request.getOldPassword());
        Admin admin = adminDAO.getById(user.getId());
        admin.setFirstName(request.getFirstName());
        admin.setLastName(request.getLastName());
        admin.setPatronymic(request.getPatronymic());
        admin.setPosition(request.getPosition());
        admin.setPassword(request.getNewPassword());
        adminDAO.update(admin);
        return adminConverter.inflateResponse(admin);
    }

}
