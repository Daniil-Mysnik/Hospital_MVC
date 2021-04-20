package net.thumbtack.school.hospital.converter;

import net.thumbtack.school.hospital.dto.request.CreateAdminRequest;
import net.thumbtack.school.hospital.dto.response.AdminResponse;
import net.thumbtack.school.hospital.model.Admin;
import org.springframework.stereotype.Component;

@Component
public class AdminConverter {

    public Admin inflateEntity(CreateAdminRequest request) {
        return new Admin(request.getFirstName(), request.getLastName(), request.getPatronymic(), request.getLogin(), request.getPassword(), request.getPosition());
    }

    public AdminResponse inflateResponse(Admin admin) {
        return new AdminResponse(admin.getId(), admin.getFirstName(), admin.getLastName(), admin.getPatronymic(), admin.getPosition(), admin.getUserType());
    }

}
