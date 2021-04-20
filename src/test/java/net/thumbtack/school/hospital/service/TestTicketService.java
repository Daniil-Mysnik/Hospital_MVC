package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.converter.DoctorConverter;
import net.thumbtack.school.hospital.converter.TicketConverter;
import net.thumbtack.school.hospital.dto.request.TicketRequest;
import net.thumbtack.school.hospital.dto.response.TicketWithOneDoctorResponse;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.*;
import net.thumbtack.school.hospital.validator.AppointmentValidator;
import net.thumbtack.school.hospital.validator.TicketValidator;
import net.thumbtack.school.hospital.validator.UserValidator;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class TestTicketService extends TestServiceBase {
    private TicketService ticketService = new TicketService(userDAO, doctorDAO, ticketDAO, appointmentDAO, new TicketConverter(),
            new UserValidator(userDAO), new AppointmentValidator(), new DoctorConverter(schedulesConverter));

    @Before
    public void initial() throws HospitalException {
        when(userDAO.getBySessionId(anyString())).thenReturn(patient);
        when(doctorDAO.getById(anyInt())).thenReturn(doctorEntity());
        when(appointmentDAO.getAppointmentByTime(anyInt(), any(LocalDate.class), any(LocalTime.class))).thenReturn(new Appointment(1, new DaySchedule(), LocalTime.of(8, 0), 15, null, null, State.FREE));
        when(doctorDAO.getRandomBySpeciality(anyString(), any(LocalDate.class), any(LocalTime.class))).thenReturn(doctorEntity());
        when(ticketDAO.getPatientIdByTicket(anyString())).thenReturn(1);
    }

    @Test
    public void testCreate() throws HospitalException {
        TicketWithOneDoctorResponse ticketResponse = (TicketWithOneDoctorResponse) ticketService.create(UUID.randomUUID().toString(), new TicketRequest(1, LocalDate.of(2020, 1, 1), LocalTime.of(8, 0)));
        assertEquals(ticketResponse.getFirstName(), doctorEntity().getFirstName());
        assertEquals(ticketResponse.getLastName(), doctorEntity().getLastName());
        assertEquals(ticketResponse.getPatronymic(), doctorEntity().getPatronymic());
        assertEquals(ticketResponse.getRoom(), doctorEntity().getRoom());
        assertEquals(ticketResponse.getSpeciality(), doctorEntity().getSpeciality());
        assertEquals(ticketResponse.getDate(), LocalDate.of(2020, 1, 1));
        assertEquals(ticketResponse.getTicket(), "D1202001010800");
    }

    @Test(expected = HospitalException.class)
    public void testCreateWithBusyAppointment() throws HospitalException {
        when(appointmentDAO.getAppointmentByTime(anyInt(), any(LocalDate.class), any(LocalTime.class))).thenReturn(new Appointment(1, new DaySchedule(), LocalTime.of(8, 0), 15, patient, "newTicket", State.TICKET));
        ticketService.create(UUID.randomUUID().toString(), new TicketRequest(2, LocalDate.of(2020, 1, 1), LocalTime.of(8, 0)));
    }

    @Test
    public void testDeleteByPatientMember() throws HospitalException {
        ticketService.delete("asodfih", "D666202001010800");
    }

    @Test
    public void testDeleteByDoctorMember() throws HospitalException {
        Doctor doctor = doctorEntity();
        doctor.setId(2);
        when(doctorDAO.getRandomBySpeciality(anyString(), any(LocalDate.class), any(LocalTime.class))).thenReturn(doctor);
        when(userDAO.getBySessionId(anyString())).thenReturn(doctor);
        ticketService.delete("asodfih", "D2202001010800");
    }

    @Test(expected = HospitalException.class)
    public void testDeleteByIncorrectPatient() throws HospitalException {
        Patient patient = this.patient;
        patient.setId(3);
        when(userDAO.getBySessionId(anyString())).thenReturn(patient);
        ticketService.delete("asodfih", "D2202001010800");
    }

}
