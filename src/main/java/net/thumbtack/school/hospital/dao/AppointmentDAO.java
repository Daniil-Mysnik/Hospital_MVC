package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.State;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentDAO {

    Appointment getAppointmentByTime(int doctorId, LocalDate date, LocalTime time) throws HospitalException;

    List<Appointment> getAppointmentsForCommission(LocalDate date, int doctorId,
                                                   LocalTime timeStart, LocalTime timeEnd) throws HospitalException;

    void updateAppointment(int appointmentId, State state) throws HospitalException;

    void updateAppointments(List<Appointment> appointments, State state) throws HospitalException;

}
