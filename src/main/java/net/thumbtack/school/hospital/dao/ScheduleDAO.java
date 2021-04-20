package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Doctor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface ScheduleDAO {

    int getDuration(int doctorId, LocalDate date) throws HospitalException;

    void checkSchedulesByRoom(String room) throws HospitalException;

    void updateSchedule(Doctor doctor, List<DaySchedule> oldDaySchedule, List<DaySchedule> newDaySchedules) throws HospitalException;

}
