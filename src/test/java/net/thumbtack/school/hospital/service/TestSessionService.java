package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.converter.AdminConverter;
import net.thumbtack.school.hospital.converter.DoctorConverter;
import net.thumbtack.school.hospital.converter.PatientConverter;
import net.thumbtack.school.hospital.dto.request.LoginRequest;
import net.thumbtack.school.hospital.dto.response.*;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Session;
import net.thumbtack.school.hospital.validator.UserValidator;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class TestSessionService extends TestServiceBase {
    SessionService sessionService = new SessionService(sessionDAO, userDAO, adminDAO, doctorDAO,
            patientDAO, ticketDAO, schedulesConverter, new AdminConverter(),
            new DoctorConverter(schedulesConverter), new PatientConverter(), new UserValidator(userDAO));

    @Before
    public void initial() throws HospitalException {
        when(adminDAO.getById(anyInt())).thenReturn(admin);
        when(patientDAO.getById(anyInt())).thenReturn(patient);
        when(doctorDAO.getById(anyInt())).thenReturn(doctorEntity());
    }

    @Test
    public void testCreateByAdmin() throws HospitalException {
        Session session = new Session(UUID.randomUUID().toString(), admin);
        when(sessionDAO.save(any(Session.class))).thenReturn(session);
        when(sessionDAO.getById(anyString())).thenReturn(session);
        when(userDAO.getByLogin(anyString())).thenReturn(admin);
        LoginResponse response = sessionService.create(new LoginRequest("popov12345", "popov12345"));
        assertNotNull(response.getSessionId());
        AdminResponse adminResponse = (AdminResponse) response.getUserResponse();
        assertEquals(admin.getFirstName(), adminResponse.getFirstName());
        assertEquals(admin.getLastName(), adminResponse.getLastName());
        assertEquals(admin.getPatronymic(), adminResponse.getPatronymic());
        assertEquals(admin.getPosition(), adminResponse.getPosition());
    }

    @Test
    public void testCreateByPatient() throws HospitalException {
        Session session = new Session(UUID.randomUUID().toString(), patient);
        when(sessionDAO.save(any(Session.class))).thenReturn(session);
        when(sessionDAO.getById(anyString())).thenReturn(session);
        when(userDAO.getByLogin(anyString())).thenReturn(patient);
        LoginResponse response = sessionService.create(new LoginRequest("sidorov12345", "sidorov12345"));
        assertNotNull(response.getSessionId());
        PatientResponse patientResponse = (PatientResponse) response.getUserResponse();
        assertEquals(patient.getFirstName(), patientResponse.getFirstName());
        assertEquals(patient.getLastName(), patientResponse.getLastName());
        assertEquals(patient.getPatronymic(), patientResponse.getPatronymic());
        assertEquals(patient.getEmail(), patientResponse.getEmail());
        assertEquals(patient.getAddress(), patientResponse.getAddress());
        assertEquals(patient.getPhone(), patientResponse.getPhone());
    }

    @Test
    public void testCreateByDoctor() throws HospitalException {
        Session session = new Session(UUID.randomUUID().toString(), doctorEntity());
        when(sessionDAO.save(any(Session.class))).thenReturn(session);
        when(sessionDAO.getById(anyString())).thenReturn(session);
        when(userDAO.getByLogin(anyString())).thenReturn(doctorEntity());
        LoginResponse response = sessionService.create(new LoginRequest("ivanov12345", "ivanov12345"));
        assertNotNull(response.getSessionId());
        DoctorWithScheduleResponse doctorResponse = (DoctorWithScheduleResponse) response.getUserResponse();
        assertEquals(doctorEntity().getFirstName(), doctorResponse.getFirstName());
        assertEquals(doctorEntity().getLastName(), doctorResponse.getLastName());
        assertEquals(doctorEntity().getPatronymic(), doctorResponse.getPatronymic());
        assertEquals(doctorEntity().getSpeciality(), doctorResponse.getSpeciality());
        assertEquals(doctorEntity().getRoom(), doctorResponse.getRoom());
        assertNotNull(doctorResponse.getSchedule());
    }

    @Test(expected = HospitalException.class)
    public void testCreateByNotExistedUser() throws HospitalException {
        sessionService.create(new LoginRequest("1!Sdfasdfsafas", "!@aafF4adfasfas"));
    }

    @Test(expected = HospitalException.class)
    public void testCreateWithNotRightPassword() throws HospitalException {
        Session session = new Session(UUID.randomUUID().toString(), doctorEntity());
        when(sessionDAO.save(any(Session.class))).thenReturn(session);
        when(sessionDAO.getById(anyString())).thenReturn(session);
        when(userDAO.getByLogin(anyString())).thenReturn(doctorEntity());
        sessionService.create(new LoginRequest("ivanov12345", "MIP&@Tgu8gn2632"));
    }

}
