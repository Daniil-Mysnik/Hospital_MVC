package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.converter.AdminConverter;
import net.thumbtack.school.hospital.dto.request.CreateAdminRequest;
import net.thumbtack.school.hospital.dto.request.UpdateAdminRequest;
import net.thumbtack.school.hospital.dto.response.AdminResponse;
import net.thumbtack.school.hospital.exceptions.HospitalErrorCode;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.validator.UserValidator;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;


public class TestAdminService extends TestServiceBase {
    private AdminService adminService = new AdminService(userDAO, adminDAO, new AdminConverter(), new UserValidator(userDAO));

    @Before
    public void initial() throws HospitalException {
        Admin mainAdmin = new Admin("Daniil", "Mysnik", "Leonidovich", "admin", "admin", "MAIN");
        when(userDAO.getBySessionId(anyString())).thenReturn(mainAdmin);
        when(adminDAO.save(any(Admin.class))).thenReturn(admin);
    }

    @Test
    public void testCreateAdmin() throws HospitalException {
        CreateAdminRequest request  = new CreateAdminRequest("Andrew", "Popov", "Alexandrovich", "popov12345", "popov12345", "Web-server");
        AdminResponse response = adminService.create(UUID.randomUUID().toString(), request);

        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getPatronymic(), response.getPatronymic());
        assertEquals(request.getPosition(), response.getPosition());
    }

    @Test(expected = HospitalException.class)
    public void testCreateAdminByNotAdmin() throws HospitalException {
        CreateAdminRequest request  = new CreateAdminRequest("Andrew", "Popov", "Alexandrovich", "popov12345", "popov12345", "Web-server");
        when(userDAO.getBySessionId(anyString())).thenThrow(new HospitalException(HospitalErrorCode.USER_IS_NOT_ADMIN));
        adminService.create(UUID.randomUUID().toString(), request);
    }

    @Test(expected = HospitalException.class)
    public void testAdminWithDuplicateLogin() throws HospitalException {
        CreateAdminRequest request  = new CreateAdminRequest("Andrew", "Popov", "Alexandrovich", "popov12345", "popov12345", "Web-server");
        Admin admin = this.admin;
        admin.setId(0);
        when(adminDAO.save(any(Admin.class))).thenReturn(admin);
        adminService.create(UUID.randomUUID().toString(), request);
        when(adminDAO.save(admin)).thenThrow(new HospitalException(HospitalErrorCode.BUSY_LOGIN));
        adminService.create(UUID.randomUUID().toString(), request);
    }

    @Test
    public void testUpdateAdmin() throws HospitalException {
        UpdateAdminRequest request = new UpdateAdminRequest("Andrew", "Popov", "Alexandrovich", "network", "popov12345", "123Popov123");
        when(userDAO.getBySessionId(anyString())).thenReturn(admin);
        when(adminDAO.getById(anyInt())).thenReturn(admin);
        AdminResponse response = adminService.update(UUID.randomUUID().toString(), request);
        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getPatronymic(), response.getPatronymic());
        assertEquals(request.getPosition(), response.getPosition());
    }

    @Test(expected = HospitalException.class)
    public void testUpdateAdminWithNotMatchPass() throws HospitalException {
        UpdateAdminRequest request = new UpdateAdminRequest("Andrew", "Popov", "Alexandrovich", "network", "JLKGwe1234123412!", "123Popov123");
        when(userDAO.getBySessionId(anyString())).thenReturn(admin);
        adminService.update(UUID.randomUUID().toString(), request);
    }

}
