package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.*;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.Assert.assertEquals;

public class TestTicketDaoImpl extends TestDaoBase {

    @Test
    public void testGetPatientIdByTicket() throws HospitalException {
        Doctor doctor = insertDoctor("Alex", "Ivanov", "Evgenievich", "1ivanov12345", "ivanov12345", "Oculist", "15a");
        Patient patient = insertPatient( "Vyacheslav", "Sidorov", null, "sidorov12345", "sidorov12345", "Sidorov@gmail.com", "Gagarina,14", "8-800-555-35-35");
        Appointment appointment = appointmentDAO.getAppointmentByTime(doctor.getId(), LocalDate.of(2020, 1, 1), LocalTime.of(8, 0));
        ticketDAO.insert(appointment.getId(), new Ticket("newTicket"), patient.getId(), State.TICKET);
        int idByTicket = ticketDAO.getPatientIdByTicket("newTicket");
        assertEquals(patient.getId(), idByTicket);
    }

    @Test(expectedExceptions = HospitalException.class)
    public void testGetPatientIdByNonExistedTicket() throws HospitalException {
        ticketDAO.getPatientIdByTicket("newTicket");
    }

    @Test(expectedExceptions = HospitalException.class)
    public void testDeleteTicket() throws HospitalException {
        Doctor doctor = insertDoctor("Alex", "Ivanov", "Evgenievich", "1ivanov12345", "ivanov12345", "Oculist", "15a");
        Patient patient = insertPatient("Vyacheslav", "Sidorov", null, "sidorov12345", "sidorov12345", "Sidorov@gmail.com", "Gagarina,14", "8-800-555-35-35");
        Appointment appointment = appointmentDAO.getAppointmentByTime(doctor.getId(), LocalDate.of(2020, 1, 1), LocalTime.of(8, 0));
        ticketDAO.insert(appointment.getId(), new Ticket("newTicket"), patient.getId(), State.TICKET);
        ticketDAO.deleteTicket("newTicket");
        ticketDAO.getPatientIdByTicket("newTicket");
    }

    @Test(expectedExceptions = HospitalException.class)
    public void testDeleteNonExistedTicket() throws HospitalException {
        ticketDAO.getPatientIdByTicket("newTicket");
    }

}
