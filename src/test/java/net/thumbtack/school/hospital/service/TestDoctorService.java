package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.converter.DoctorConverter;
import net.thumbtack.school.hospital.dto.request.CreateDoctorRequest;
import net.thumbtack.school.hospital.dto.request.UpdateScheduleRequest;
import net.thumbtack.school.hospital.dto.response.BusyAppointmentResponse;
import net.thumbtack.school.hospital.dto.response.DoctorResponse;
import net.thumbtack.school.hospital.dto.response.DoctorWithScheduleResponse;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.validator.AppointmentValidator;
import net.thumbtack.school.hospital.validator.UserValidator;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class TestDoctorService extends TestServiceBase {
    private DoctorService doctorService = new DoctorService(userDAO, doctorDAO, scheduleDAO, ticketDAO, new DoctorConverter(schedulesConverter), new UserValidator(userDAO), schedulesConverter, new AppointmentValidator());

    @Before
    public void initial() throws HospitalException {
        when(userDAO.getBySessionId(anyString())).thenReturn(admin);
        when(doctorDAO.getById(anyInt())).thenReturn(doctorEntity());
    }

    @Test
    public void testCreateDoctorWithFirstTypeSchedule() throws Exception {
        CreateDoctorRequest request = doctorRequest();
        request.setWeekDaysSchedule(null);

        when(doctorDAO.save(any(Doctor.class))).thenReturn(doctorEntity());
        DoctorWithScheduleResponse response = doctorService.create(UUID.randomUUID().toString(), request);
        assertEquals(doctorRequest().getFirstName(), response.getFirstName());
        assertEquals(doctorRequest().getLastName(), response.getLastName());
        assertEquals(doctorRequest().getPatronymic(), response.getPatronymic());
        assertEquals(doctorRequest().getSpeciality(), response.getSpeciality());
        assertEquals(doctorRequest().getRoom(), response.getRoom());
        assertNotNull(response.getSchedule());
        assertEquals(2, response.getSchedule().size());
        assertNotNull(response.getSchedule().get(0).getDaySchedule());
    }

    @Test
    public void testCreateDoctorWithSecondTypeSchedule() throws HospitalException {
        CreateDoctorRequest request = doctorRequest();
        request.setWeekSchedule(null);

        when(doctorDAO.save(any(Doctor.class))).thenReturn(doctorEntity());
        DoctorWithScheduleResponse response = doctorService.create(UUID.randomUUID().toString(), request);
        assertEquals(doctorRequest().getFirstName(), response.getFirstName());
        assertEquals(doctorRequest().getLastName(), response.getLastName());
        assertEquals(doctorRequest().getPatronymic(), response.getPatronymic());
        assertEquals(doctorRequest().getSpeciality(), response.getSpeciality());
        assertEquals(doctorRequest().getRoom(), response.getRoom());
        assertNotNull(response.getSchedule());
        assertEquals(2, response.getSchedule().size());
        assertNotNull(response.getSchedule().get(0).getDaySchedule());
    }

    @Test(expected = HospitalException.class)
    public void testCreateDoctorByPatient() throws HospitalException {
        when(userDAO.getBySessionId(anyString())).thenReturn(patient);
        doctorService.create(UUID.randomUUID().toString(), doctorRequest());
    }

    @Test(expected = HospitalException.class)
    public void testCreateDoctorByDoctor() throws HospitalException {
        when(userDAO.getBySessionId(anyString())).thenReturn(doctorEntity());
        doctorService.create(UUID.randomUUID().toString(), doctorRequest());
    }

//    @Test
//    public void testGetDoctorWithoutSchedule() throws HospitalException {
//        DoctorResponse response = doctorService.get(UUID.randomUUID().toString(), 2, "no", null, null);
//        assertEquals(doctorEntity().getFirstName(), response.getFirstName());
//        assertEquals(doctorEntity().getLastName(), response.getLastName());
//        assertEquals(doctorEntity().getPatronymic(), response.getPatronymic());
//        assertEquals(doctorEntity().getSpeciality(), response.getSpeciality());
//        assertEquals(doctorEntity().getRoom(), response.getRoom());
//    }

//    @Test
//    public void testGetDoctorWithSchedule() throws HospitalException {
//        DoctorWithScheduleResponse response = (DoctorWithScheduleResponse) doctorService.get(UUID.randomUUID().toString(), 2, "yes", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1));
//        assertEquals(doctorEntity().getFirstName(), response.getFirstName());
//        assertEquals(doctorEntity().getLastName(), response.getLastName());
//        assertEquals(doctorEntity().getPatronymic(), response.getPatronymic());
//        assertEquals(doctorEntity().getSpeciality(), response.getSpeciality());
//        assertEquals(doctorEntity().getRoom(), response.getRoom());
//        assertEquals(1, response.getSchedule().size());
//        assertEquals(LocalDate.of(2020, 1, 1), response.getSchedule().get(0).getDate());
//        assertEquals(2, response.getSchedule().get(0).getDaySchedule().size());
//    }

//    @Test
//    public void testGetDoctorByScheduledPatient() throws HospitalException {
//        Doctor doctor = doctorEntity();
//        Patient patient = this.patient;
//        patient.setId(3);
//        doctor.getDayScheduleList().get(0).getAppointments().get(0).setPatient(patient);
//        doctor.getDayScheduleList().get(0).getAppointments().get(0).setTicket("newTicket");
//        when(userDAO.getBySessionId(anyString())).thenReturn(patient);
//        when(doctorDAO.getById(anyInt())).thenReturn(doctor);
//        DoctorWithScheduleResponse response = (DoctorWithScheduleResponse) doctorService.get(UUID.randomUUID().toString(), 2, "yes", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 1));
//        BusyAppointmentResponse appointmentResponse = (BusyAppointmentResponse) response.getSchedule().get(0).getDaySchedule().get(0);
//        assertEquals(patient.getFirstName(), appointmentResponse.getPatient().getFirstName());
//        assertEquals(patient.getLastName(), appointmentResponse.getPatient().getLastName());
//        assertEquals(patient.getPatronymic(), appointmentResponse.getPatient().getPatronymic());
//        assertEquals(patient.getPhone(), appointmentResponse.getPatient().getPhone());
//        assertEquals(patient.getAddress(), appointmentResponse.getPatient().getAddress());
//        assertEquals(patient.getEmail(), appointmentResponse.getPatient().getEmail());
//    }

//    @Test
//    public void testGetDoctorBySpeciality() throws HospitalException {
//        Doctor doctor1 = doctorEntity();
//        doctor1.setId(1);
//        Doctor doctor2 = doctorEntity();
//        doctor2.setId(2);
//        Doctor doctor3 = doctorEntity();
//        doctor3.setSpeciality("fioasufgasu");
//        Doctor doctor4 = doctorEntity();
//        doctor4.setSpeciality("fioasufgasu");
//        when(doctorDAO.getIdsBySpeciality("Oculist")).thenReturn(Arrays.asList(1, 2));
//        when( doctorDAO.getAllIds()).thenReturn(Arrays.asList(1, 2, 3, 4));
//        List<DoctorResponse> doctorResponses = doctorService.getRsBySpeciality(UUID.randomUUID().toString(), "yes", "Oculist", LocalDate.of(2020, 1, 1), null);
//        assertEquals(2, doctorResponses.size());
//    }

    @Test
    public void testUpdateDoctorWithFirstTypeSchedule() throws HospitalException {
        UpdateScheduleRequest request = new UpdateScheduleRequest(LocalDate.of(2020, 1, 2), LocalDate.of(2020, 1, 3), weekScheduleRequest(), null, 20);
        DoctorWithScheduleResponse response = (DoctorWithScheduleResponse) doctorService.update(UUID.randomUUID().toString(), 2, request);
        assertEquals(3, response.getSchedule().size());
        assertEquals(LocalDate.of(2020, 1, 1), response.getSchedule().get(0).getDate());
        assertEquals(LocalDate.of(2020, 1, 2), response.getSchedule().get(1).getDate());
        assertEquals(LocalDate.of(2020, 1, 3), response.getSchedule().get(2).getDate());
    }

    @Test
    public void testUpdateDoctorWithSecondTypeSchedule() throws HospitalException {
        UpdateScheduleRequest request = new UpdateScheduleRequest(LocalDate.of(2020, 1, 2), LocalDate.of(2020, 1, 3), null, weekDaysScheduleRequest(), 20);
        DoctorWithScheduleResponse response = (DoctorWithScheduleResponse) doctorService.update(UUID.randomUUID().toString(), 2, request);
        assertEquals(3, response.getSchedule().size());
        assertEquals(LocalDate.of(2020, 1, 1), response.getSchedule().get(0).getDate());
        assertEquals(LocalDate.of(2020, 1, 2), response.getSchedule().get(1).getDate());
        assertEquals(LocalDate.of(2020, 1, 3), response.getSchedule().get(2).getDate());
    }

    @Test(expected = HospitalException.class)
    public void testUpdateByPatient() throws HospitalException {
        when(userDAO.getBySessionId(anyString())).thenReturn(patient);
        UpdateScheduleRequest request = new UpdateScheduleRequest(LocalDate.of(2020, 1, 2), LocalDate.of(2020, 1, 3), weekScheduleRequest(), null, 20);
        doctorService.update(UUID.randomUUID().toString(), 2, request);
    }

    @Test(expected = HospitalException.class)
    public void testUpdateByDoctor() throws HospitalException {
        when(userDAO.getBySessionId(anyString())).thenReturn(doctorEntity());
        UpdateScheduleRequest request = new UpdateScheduleRequest(LocalDate.of(2020, 1, 2), LocalDate.of(2020, 1, 3), weekScheduleRequest(), null, 20);
        doctorService.update(UUID.randomUUID().toString(), 2, request);
    }

    @Test
    public void testDeleteByAdmin() throws HospitalException {
        doctorService.delete(UUID.randomUUID().toString(), 2, LocalDate.of(2020, 1, 1));
    }

    @Test(expected = HospitalException.class)
    public void testDeleteByDoctor() throws HospitalException {
        when(userDAO.getBySessionId(anyString())).thenReturn(doctorEntity());
        doctorService.delete(UUID.randomUUID().toString(), 2, LocalDate.of(2020, 1, 1));
    }

    @Test(expected = HospitalException.class)
    public void testDeleteByPatient() throws HospitalException {
        when(userDAO.getBySessionId(anyString())).thenReturn(patient);
        doctorService.delete(UUID.randomUUID().toString(), 2, LocalDate.of(2020, 1, 1));
    }

}
