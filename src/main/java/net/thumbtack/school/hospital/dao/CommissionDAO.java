package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.Ticket;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface CommissionDAO {

    void insert(int patientId, LocalDate date, LocalTime time, int duration, List<Appointment> appointments, Ticket ticket) throws HospitalException;

}
