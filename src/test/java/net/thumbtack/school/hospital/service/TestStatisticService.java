package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.converter.DoctorConverter;
import net.thumbtack.school.hospital.converter.PatientConverter;
import net.thumbtack.school.hospital.converter.TicketConverter;
import net.thumbtack.school.hospital.dto.response.*;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.State;
import net.thumbtack.school.hospital.model.Ticket;
import net.thumbtack.school.hospital.validator.UserValidator;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class TestStatisticService extends TestServiceBase {
    private StatisticService statisticService = new StatisticService(doctorDAO, ticketDAO, ticketDAO, patientDAO, userDAO,
            schedulesConverter, new DoctorConverter(schedulesConverter), new TicketConverter(), new PatientConverter(), new UserValidator(userDAO));

    @Test
    public void testGetDoctorWithFreeTickets() throws HospitalException {
        Doctor doctor = doctorEntity();
        when(doctorDAO.getById(anyInt())).thenReturn(doctor);
        DoctorWithScheduleResponse response = (DoctorWithScheduleResponse) statisticService.getDoctorWithFreeTickets(1, "yes", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 2));
        assertEquals(doctor.getFirstName(), response.getFirstName());
        assertEquals(doctor.getLastName(), response.getLastName());
        assertEquals(doctor.getPatronymic(), response.getPatronymic());
        assertEquals(doctor.getRoom(), response.getRoom());
        assertEquals(doctor.getSpeciality(), response.getSpeciality());
        assertEquals(4, response.getSchedule().get(0).getDaySchedule().size() + response.getSchedule().get(1).getDaySchedule().size());
        CountOfFreeAppointmentsResponse response1 = (CountOfFreeAppointmentsResponse) statisticService.getDoctorWithFreeTickets(1, "no", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 2));
        assertEquals(doctor.getFirstName(), response1.getFirstName());
        assertEquals(doctor.getLastName(), response1.getLastName());
        assertEquals(doctor.getPatronymic(), response1.getPatronymic());
        assertEquals(doctor.getRoom(), response1.getRoom());
        assertEquals(doctor.getSpeciality(), response1.getSpeciality());
        assertEquals(4, response1.getCount());
    }

    @Test
    public void testGetDoctorWithFreeTicketsWhenSomeBusy() throws HospitalException {
        Doctor doctor = doctorEntity();
        doctor.getDayScheduleList().get(0).getAppointments().get(1).setState(State.TICKET);
        doctor.getDayScheduleList().get(1).getAppointments().get(1).setState(State.TICKET);
        when(doctorDAO.getById(anyInt())).thenReturn(doctor);
        DoctorWithScheduleResponse response = (DoctorWithScheduleResponse) statisticService.getDoctorWithFreeTickets(1, "yes", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 2));
        assertEquals(2, response.getSchedule().get(0).getDaySchedule().size() + response.getSchedule().get(1).getDaySchedule().size());
        CountOfFreeAppointmentsResponse response1 = (CountOfFreeAppointmentsResponse) statisticService.getDoctorWithFreeTickets(1, "no", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 2));
        assertEquals(2, response1.getCount());
    }

    @Test
    public void testGetAllDoctorsWithFreeTickets() throws HospitalException {
        Doctor doctor = doctorEntity();
        doctor.getDayScheduleList().get(0).getAppointments().get(1).setState(State.TICKET);
        doctor.getDayScheduleList().get(1).getAppointments().get(1).setState(State.TICKET);
        when(doctorDAO.getById(anyInt())).thenReturn(doctor);
        when(doctorDAO.getAllIds()).thenReturn(Arrays.asList(doctor.getId()));
        List<DoctorResponse> response = statisticService.getAllDoctorsWithFreeTickets("yes", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 2));
        assertEquals(1, response.size());
    }

    @Test
    public void testGetPatientWithHisAppointments() throws HospitalException {
        when(userDAO.getBySessionId(anyString())).thenReturn(doctorEntity());
        when(patientDAO.getById(anyInt())).thenReturn(patient);
        List<Ticket> tickets = new ArrayList<>(3);
        tickets.add(new Ticket("Ticket1"));
        tickets.add(new Ticket("Ticket2"));
        tickets.add(new Ticket("Ticket3"));
        when(ticketDAO.getPatientTickets(anyInt(), any(), any())).thenReturn(tickets);
        BusyTicketsResponse response = (BusyTicketsResponse) statisticService.getPatientWithHisAppointments("qwerwqe", 1, "yes", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 2));
        assertEquals(patient.getFirstName(), response.getFirstName());
        assertEquals(patient.getLastName(), response.getLastName());
        assertEquals(patient.getPatronymic(), response.getPatronymic());
        assertEquals(patient.getPhone(), response.getPhone());
        assertEquals(patient.getEmail(), response.getEmail());
        assertEquals(patient.getAddress(), response.getAddress());
        assertEquals("Ticket1", response.getBusyTickets().get(0).getTicket());
        assertEquals("Ticket2", response.getBusyTickets().get(1).getTicket());
        assertEquals("Ticket3", response.getBusyTickets().get(2).getTicket());
        CountOfBusyAppointmentsResponse response1 = (CountOfBusyAppointmentsResponse) statisticService.getPatientWithHisAppointments("qwerwqe", 1, "no", LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 2));
        assertEquals(patient.getFirstName(), response1.getFirstName());
        assertEquals(patient.getLastName(), response1.getLastName());
        assertEquals(patient.getPatronymic(), response1.getPatronymic());
        assertEquals(patient.getPhone(), response1.getPhone());
        assertEquals(patient.getEmail(), response1.getEmail());
        assertEquals(patient.getAddress(), response1.getAddress());
        assertEquals(3, response1.getCount());
    }
}
