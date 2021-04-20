package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.*;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.Assert.*;

public class TestAppointmentDaoImpl extends TestDaoBase {

    @Test
    public void testGetAppointmentByTime() throws HospitalException {
        Doctor doctor = insertDoctor("Alex", "Ivanov", null, "ivanov12345", "ivanov12345", "Oculist", "15a");
        Appointment appointment = appointmentDAO.getAppointmentByTime(doctor.getId(), LocalDate.of(2020, 1, 2), LocalTime.of(8, 0));
        assertNotNull(appointment);
        assertEquals(LocalTime.of(8, 0), appointment.getTime());
        assertNull(appointment.getPatient());
        assertNull(appointment.getTicket());
    }

    @Test
    public void testGetAppointmentsForCommission() throws HospitalException {
        Doctor doctor = insertDoctor("Alex", "Ivanov", null, "ivanov12345", "ivanov12345", "Oculist", "15a");
        List<Appointment> appointments = appointmentDAO.getAppointmentsForCommission(LocalDate.of(2020, 1, 2), doctor.getId(), LocalTime.of(8, 0), LocalTime.of(8, 20));
        assertEquals(2, appointments.size());
        assertEquals(LocalTime.of(8, 0), appointments.get(0).getTime());
        assertEquals(LocalTime.of(8, 15), appointments.get(1).getTime());
    }

    @Test
    public void testUpdateAppointment() throws HospitalException {
        Doctor doctor = insertDoctor("Alex", "Ivanov", null, "ivanov12345", "ivanov12345", "Oculist", "15a");
        Patient patient = insertPatient("Vyacheslav", "Sidorov", null, "sidorov12345", "sidorov12345", "Sidorov@gmail.com", "Gagarina,14", "8-800-555-35-35");
        Appointment appointment = appointmentDAO.getAppointmentByTime(doctor.getId(), LocalDate.of(2020, 1, 1), LocalTime.of(8, 0));
        ticketDAO.insert(appointment.getId(), new Ticket("newTicket"), patient.getId(), State.TICKET);
        Appointment updatedAppointment = appointmentDAO.getAppointmentByTime(doctor.getId(), LocalDate.of(2020, 1, 1), LocalTime.of(8, 0));
        assertNotEquals(appointment, updatedAppointment);
        assertEquals(State.TICKET, updatedAppointment.getState());
    }

    @Test
    public void testUpdateAppointments() throws HospitalException {
        Doctor doctor = insertDoctor("Alex", "Ivanov", null, "ivanov12345", "ivanov12345", "Oculist", "15a");
        List<Appointment> appointments = appointmentDAO.getAppointmentsForCommission(LocalDate.of(2020, 1, 2), doctor.getId(), LocalTime.of(8, 0), LocalTime.of(8, 20));
        appointmentDAO.updateAppointments(appointments, State.COMMISSION);
        List<Appointment> updatedAppointments = appointmentDAO.getAppointmentsForCommission(LocalDate.of(2020, 1, 2), doctor.getId(), LocalTime.of(8, 0), LocalTime.of(8, 20));
        assertNotEquals(appointments, updatedAppointments);
        assertEquals(State.COMMISSION, updatedAppointments.get(0).getState());
        assertEquals(State.COMMISSION, updatedAppointments.get(1).getState());

    }

}
