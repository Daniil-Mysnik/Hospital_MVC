package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.State;
import org.testng.annotations.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestDayScheduleDaoImpl extends TestDaoBase {

    @Test
    public void testGetDuration() throws HospitalException {
        Doctor doctor = insertDoctor("Alex", "Ivanov", "Evgenievich", "ivanov12345", "ivanov12345", "Oculist", "15a");
        int duration = scheduleDAO.getDuration(doctor.getId(), LocalDate.of(2020, 1, 1));
        assertEquals(15, duration);
    }

    @Test(expectedExceptions = HospitalException.class)
    public void testGetSchedulesByRoom() throws HospitalException {
        Doctor doctor = insertDoctor("Alex", "Ivanov", "Evgenievich", "ivanov12345", "ivanov12345", "Oculist", "15a");
        scheduleDAO.checkSchedulesByRoom(doctor.getRoom());
    }

    @Test
    public void testUpdateFreeSchedule() throws HospitalException {
        Doctor doctor = insertDoctor("Alex", "Ivanov", "Evgenievich", "ivanov12345", "ivanov12345", "Oculist", "15a");
        List<DaySchedule> daySchedules = doctor.getDayScheduleList();
        List<DaySchedule> oldDaySchedules = new ArrayList<>();
        oldDaySchedules.add(daySchedules.get(1));
        List<Appointment> appointments = new ArrayList<>();
        Appointment appointment1 = new Appointment(LocalTime.of(9, 0), 20, State.FREE);
        Appointment appointment2 = new Appointment(LocalTime.of(9, 30), 20, State.FREE);
        appointments.add(appointment1);
        appointments.add(appointment2);
        List<DaySchedule> newDaySchedules = new ArrayList<>();
        DaySchedule daySchedule = new DaySchedule(LocalDate.of(2020, 1, 2), null, null, appointments);
        newDaySchedules.add(daySchedule);
        for (DaySchedule daySchedule1 : newDaySchedules) {
            daySchedule1.setDoctor(doctor);
            for (Appointment appointment : daySchedule1.getAppointments()) {
                appointment.setDaySchedule(daySchedule1);
            }
        }
        scheduleDAO.updateSchedule(doctor, oldDaySchedules, newDaySchedules);
        Doctor doctorWithUpdatedSchedule = doctorDAO.getById(doctor.getId());
        assertNotEquals(daySchedules, doctorWithUpdatedSchedule.getDayScheduleList());
        assertEquals(doctorWithUpdatedSchedule.getDayScheduleList().get(0).getDate(), LocalDate.of(2020, 1, 1));
        assertEquals(doctorWithUpdatedSchedule.getDayScheduleList().get(0).getAppointments().get(0).getTime(), LocalTime.of(8, 0));
        assertEquals(doctorWithUpdatedSchedule.getDayScheduleList().get(0).getAppointments().get(1).getTime(), LocalTime.of(8, 15));
    }

}
