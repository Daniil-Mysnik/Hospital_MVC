package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dto.request.CommissionRequest;
import net.thumbtack.school.hospital.dto.response.CommissionResponse;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.validator.CommissionValidator;
import net.thumbtack.school.hospital.validator.UserValidator;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class TestCommissionService extends TestServiceBase {
    private CommissionService commissionService = new CommissionService(userDAO, doctorDAO, appointmentDAO, scheduleDAO,
            patientDAO, commissionDAO , new UserValidator(userDAO), new CommissionValidator());

    @Before
    public void initial() throws HospitalException {
        Doctor doctor = doctorEntity();
        doctor.setId(2);
        when(userDAO.getBySessionId(anyString())).thenReturn(doctor);
        when(doctorDAO.getRoomByDoctorId(anyInt())).thenReturn("15a");
        when(scheduleDAO.getDuration(anyInt(), any(LocalDate.class))).thenReturn(15);
        when(appointmentDAO.getAppointmentsForCommission(any(LocalDate.class), anyInt(), any(LocalTime.class), any(LocalTime.class))).thenReturn(appointments());
    }

    @Test
    public void testCreate() throws HospitalException {
        CommissionResponse response = commissionService.create(UUID.randomUUID().toString(), commissionRequest());
        assertEquals(response.getTicket(), "CD2D3D4202001010800");
        assertEquals(response.getRoom(), commissionRequest().getRoom());
        assertEquals(response.getDoctorIds(), Arrays.asList(2, 3, 4));
        assertEquals(response.getDuration(), commissionRequest().getDuration());
        assertEquals(response.getDate(), commissionRequest().getDate());
        assertEquals(response.getPatientId(), commissionRequest().getPatientId());
    }

    @Test(expected = HospitalException.class)
    public void testCreateByPatient() throws HospitalException {
        when(userDAO.getBySessionId(anyString())).thenReturn(patient);
        commissionService.create(UUID.randomUUID().toString(), commissionRequest());
    }

    @Test(expected = HospitalException.class)
    public void testCreateByAdmin() throws HospitalException {
        when(userDAO.getBySessionId(anyString())).thenReturn(admin);
        commissionService.create(UUID.randomUUID().toString(), commissionRequest());
    }

    @Test(expected = HospitalException.class)
    public void testCreateInNonWorkingTime() throws HospitalException {
        CommissionRequest request = commissionRequest();
        request.setTime(LocalTime.of(0, 0));
        commissionService.create(UUID.randomUUID().toString(), request);
    }

}
