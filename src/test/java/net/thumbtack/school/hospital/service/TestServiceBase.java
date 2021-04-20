package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.converter.AppointmentConverter;
import net.thumbtack.school.hospital.converter.PatientConverter;
import net.thumbtack.school.hospital.converter.SchedulesConverter;
import net.thumbtack.school.hospital.dao.*;
import net.thumbtack.school.hospital.dto.request.*;
import net.thumbtack.school.hospital.model.*;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestServiceBase {
    protected UserDAO userDAO = Mockito.mock(UserDAO.class);

    protected AdminDAO adminDAO = Mockito.mock(AdminDAO.class);

    protected PatientDAO patientDAO =  Mockito.mock(PatientDAO.class);

    protected DoctorDAO doctorDAO = Mockito.mock(DoctorDAO.class);

    protected ScheduleDAO scheduleDAO = Mockito.mock(ScheduleDAO.class);

    protected AppointmentDAO appointmentDAO = Mockito.mock(AppointmentDAO.class);

    protected SessionDAO sessionDAO = Mockito.mock(SessionDAO.class);

    protected TicketDAO ticketDAO = Mockito.mock(TicketDAO.class);

    protected CommissionDAO commissionDAO = Mockito.mock(CommissionDAO.class);

    private PatientConverter patientConverter = new PatientConverter();

    protected SchedulesConverter schedulesConverter = new SchedulesConverter(patientConverter, new AppointmentConverter(patientConverter));

    protected Admin admin = new Admin(1, "Andrew", "Popov", "Alexandrovich", "popov12345", "popov12345", "Web-server");

    protected Patient patient = new Patient(1, "Vyacheslav", "Sidorov", null, "sidorov12345", "sidorov12345", "Sidorov@gmail.com", "Gagarina,14", "8-800-555-35-35");

    protected Doctor doctorEntity() {
        Appointment appointment1 = new Appointment(LocalTime.of(8, 0), 15, State.FREE);
        Appointment appointment2 = new Appointment(LocalTime.of(8, 15), 15, State.FREE);
        List<Appointment> appointments = new ArrayList<>(2);
        appointments.add(appointment1);
        appointments.add(appointment2);
        DaySchedule daySchedule1 = new DaySchedule(LocalDate.of(2020, 1, 1), null, null, appointments);
        DaySchedule daySchedule2 = new DaySchedule(LocalDate.of(2020, 1, 2), null, null, appointments);
        List<DaySchedule> daySchedules = new ArrayList<>(2);
        daySchedules.add(daySchedule1);
        daySchedules.add(daySchedule2);
        return new Doctor(1,"Alex", "Ivanov", null, "ivanov12345", "ivanov12345", "Oculist", "15a", daySchedules);
    }

    protected CreateDoctorRequest doctorRequest() {
        return new CreateDoctorRequest(LocalDate.of(2020, 1, 1), LocalDate.of(2020, 1, 10),
                weekScheduleRequest(), weekDaysScheduleRequest(), 15, "Alex","Ivanov",
                null, "Oculist", "15a", "ivanov12345", "ivanov12345");
    }

    protected WeekScheduleRequest weekScheduleRequest() {
        List<String> weekDays = new ArrayList<>(3);
        weekDays.add("Wed");
        weekDays.add("Thu");
        weekDays.add("Fri");
        return new WeekScheduleRequest(LocalTime.of(8, 0), LocalTime.of(10, 0), weekDays);
    }

    protected WeekDaysScheduleRequest weekDaysScheduleRequest() {
        List<AppointmentRequest> appointmentRequests = new ArrayList<>(3);
        appointmentRequests.add(new AppointmentRequest("Wed", LocalTime.of(8, 0), LocalTime.of(10, 0)));
        appointmentRequests.add(new AppointmentRequest("Thu", LocalTime.of(9, 0), LocalTime.of(11, 0)));
        appointmentRequests.add(new AppointmentRequest("Fri", LocalTime.of(10, 0), LocalTime.of(12, 0)));
        return new WeekDaysScheduleRequest(appointmentRequests);
    }

    protected CommissionRequest commissionRequest() {
        List<Integer> doctorIds = new ArrayList<>();
        doctorIds.add(3);
        doctorIds.add(4);
        doctorIds.add(2);
        return new CommissionRequest(1, doctorIds, "15a", LocalDate.of(2020, 1, 1), LocalTime.of(8, 0),40);
    }

    protected List<Appointment> appointments() {
        Appointment a1 = new Appointment(LocalTime.of(8, 0), 15, State.FREE);
        Appointment a2 = new Appointment(LocalTime.of(8, 45), 15, State.FREE);
        return Arrays.asList(a1, a2);
    }

}
