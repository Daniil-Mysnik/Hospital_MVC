package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.dao.impl.*;
import net.thumbtack.school.hospital.debug.dao.DebugDAO;
import net.thumbtack.school.hospital.debug.dao.DebugDAOImpl;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.*;
import org.junit.jupiter.api.Assertions;
import org.testng.annotations.BeforeMethod;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertNotEquals;

public class TestDaoBase {
    protected AdminDAO adminDAO = new AdminDAOImpl();
    protected AppointmentDAO appointmentDAO = new AppointmentDAOImpl();
    protected DoctorDAO doctorDAO = new DoctorDAOImpl();
    protected PatientDAO patientDAO = new PatientDAOImpl();
    protected SessionDAO sessionDAO = new SessionDAOImpl();
    protected ScheduleDAO scheduleDAO = new ScheduleDAOImpl();
    protected UserDAO userDAO = new UserDAOImpl();
    protected TicketDAO ticketDAO = new TicketDAOImpl();
    protected CommissionDAO commissionDAO = new CommissionDAOImpl();
    private DebugDAO debugDAO = new DebugDAOImpl();

    @BeforeMethod()
    public void clearDB() {
        debugDAO.clear();
    }

    protected Admin insertAdmin(String firstName, String lastName, String patronymic, String login, String password, String position) throws HospitalException {
        Admin admin = new Admin(firstName, lastName, patronymic, login, password, position);
        adminDAO.save(admin);
        assertNotEquals(0, admin.getId());
        return admin;
    }

    protected Doctor insertDoctor(String firstName, String lastName, String patronymic, String login, String password, String speciality, String room) throws HospitalException {
        Appointment appointment1 = new Appointment(LocalTime.of(8, 0), 15, State.FREE);
        Appointment appointment2 = new Appointment(LocalTime.of(8, 15), 15, State.FREE);
        List<Appointment> appointments = new ArrayList<>(2);
        appointments.add(appointment1);
        appointments.add(appointment2);
        Appointment appointment3 = new Appointment(LocalTime.of(8, 0), 15, State.FREE);
        Appointment appointment4 = new Appointment(LocalTime.of(8, 15), 15, State.FREE);
        List<Appointment> appointments2 = new ArrayList<>(2);
        appointments2.add(appointment3);
        appointments2.add(appointment4);
        DaySchedule daySchedule1 = new DaySchedule(LocalDate.of(2020, 1, 1), null, null, appointments);
        DaySchedule daySchedule2 = new DaySchedule(LocalDate.of(2020, 1, 2),null, null, appointments2);
        List<DaySchedule> daySchedules = new ArrayList<>(2);
        daySchedules.add(daySchedule1);
        daySchedules.add(daySchedule2);
        Doctor doctor = new Doctor(firstName, lastName, patronymic, login, password, speciality, room, daySchedules);
        for (DaySchedule daySchedule : doctor.getDayScheduleList()) {
            daySchedule.setDoctor(doctor);
            for (Appointment appointment : daySchedule.getAppointments()) {
                appointment.setDaySchedule(daySchedule);
            }
        }
        Doctor doctorFromDB = doctorDAO.save(doctor);
        Assertions.assertNotEquals(0, doctorFromDB.getId());
        return doctorFromDB;
    }

    protected Patient insertPatient(String firstName, String lastName, String patronymic, String login, String password, String email, String address, String phone) throws HospitalException {
        Patient patient = new Patient(firstName, lastName, patronymic, login, password, email, address, phone);
        patient = patientDAO.save(patient);
        assertNotEquals(0, patient.getId());
        return patient;
    }

    protected void saveSession(int userId) throws HospitalException {
        Session request = new Session(UUID.randomUUID().toString(), userDAO.getById(userId));
        sessionDAO.save(request);
    }

    protected Session saveSessionById(String sessionId) throws HospitalException {
        Session request = new Session(sessionId, userDAO.getById(1));
        return sessionDAO.save(request);
    }

}
