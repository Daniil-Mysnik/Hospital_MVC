package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.dto.response.TicketWithOneDoctorResponse;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.State;
import net.thumbtack.school.hospital.model.Ticket;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TicketDAO {

    void insert(int appointmentId, Ticket ticket, int patientId, State state) throws HospitalException;

    int getPatientIdByTicket(String ticketNumber) throws HospitalException;

    List<String> getTicketsByPatientId(int patientId) throws HospitalException;

    List<Ticket> getTicketByDoctorAndDate(int doctorId, LocalDate date) throws HospitalException;

    List<Ticket> getPatientTickets(int patientId, LocalDate dateStart, LocalDate dateEnd) throws HospitalException;

    List<TicketWithOneDoctorResponse> getAllInfoByTicket(String ticketNumber) throws HospitalException;

    Ticket getTicket(int doctorId, LocalDate date, LocalTime time) throws HospitalException;

    Ticket getCommissionTicket(int doctorId, LocalDate date, LocalTime time) throws HospitalException;

    void deleteTicket(String ticketNumber) throws HospitalException;

}
