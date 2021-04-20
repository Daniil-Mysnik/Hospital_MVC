package net.thumbtack.school.hospital.dao.impl;

import net.thumbtack.school.hospital.dao.TicketDAO;
import net.thumbtack.school.hospital.dto.response.TicketWithOneDoctorResponse;
import net.thumbtack.school.hospital.exceptions.HospitalErrorCode;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.State;
import net.thumbtack.school.hospital.model.Ticket;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TicketDAOImpl extends MyBatisHelper implements TicketDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(TicketDAOImpl.class);

    @Override
    public void insert(int appointmentId, Ticket ticket, int patientId, State state) throws HospitalException {
        LOGGER.debug("DAO create ticket");
        try (SqlSession sqlSession = getSession()) {
            try {
                int res = getAppointmentMapper(sqlSession).update(appointmentId, state);
                if (res == 0) {
                    throw new HospitalException(HospitalErrorCode.BUSY_TICKET);
                }
                getTicketMapper(sqlSession).insert(ticket, patientId);
                getTicketMapper(sqlSession).insertAppointmentTicket(ticket.getId(), appointmentId);
            } catch (RuntimeException e) {
                LOGGER.error("Can't create ticket", e);
                sqlSession.rollback();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

    @Override
    public int getPatientIdByTicket(String ticketNumber) throws HospitalException {
        LOGGER.debug("DAO get patient id by ticket with number = {}", ticketNumber);
        try (SqlSession sqlSession = getSession()) {
            try {
                Integer patientId = getTicketMapper(sqlSession).getPatientIdByTicket(ticketNumber);
                if (patientId == null)
                    throw new HospitalException(HospitalErrorCode.TICKET_NOT_EXISTS);
                return patientId;
            } catch (RuntimeException e) {
                LOGGER.error("Can't patient id by ticket with number = {}", ticketNumber);
                e.printStackTrace();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public List<String> getTicketsByPatientId(int patientId) throws HospitalException {
        LOGGER.debug("DAO get tickets id by patient with id = {}", patientId);
        try (SqlSession sqlSession = getSession()) {
            try {
                List<String> tickets = getTicketMapper(sqlSession).getTicketsByPatientId(patientId);
                if (tickets.size() == 0)
                    throw new HospitalException(HospitalErrorCode.PATIENT_HAS_NO_TICKET);
                return tickets;
            } catch (RuntimeException e) {
                LOGGER.error("Can't tickets id by patient with id = {}", patientId);
                e.printStackTrace();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public List<Ticket> getTicketByDoctorAndDate(int doctorId, LocalDate date) throws HospitalException {
        LOGGER.debug("DAO get tickets id by doctor id = {} and time = {}", doctorId, date);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getTicketMapper(sqlSession).getTicketByDoctorAndDate(doctorId, date);
            } catch (RuntimeException e) {
                LOGGER.error("Can't get tickets id by doctor id = {} and time = {}", doctorId, date);
                e.printStackTrace();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public List<Ticket> getPatientTickets(int patientId, LocalDate dateStart, LocalDate dateEnd) throws HospitalException {
        LOGGER.debug("DAO get tickets of patient with id = {}", patientId);
        try (SqlSession sqlSession = getSession()) {
            try {
                List<Ticket> tickets = getTicketMapper(sqlSession).getTicketsByTerm(patientId, dateStart, dateEnd);
                List<Ticket> commissionTickets = getTicketMapper(sqlSession).getCommissionTicketsByTerm(patientId, dateStart, dateEnd);
                tickets.addAll(commissionTickets);
                return tickets.stream().sorted(Comparator.comparing(Ticket::getDate)).collect(Collectors.toList());
            } catch (RuntimeException e) {
                LOGGER.error("Can't get tickets of patient with id = {}", patientId);
                e.printStackTrace();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public List<TicketWithOneDoctorResponse> getAllInfoByTicket(String ticketNumber) throws HospitalException {
        LOGGER.debug("DAO get all info about appointment by ticket with number {}", ticketNumber);
        try (SqlSession sqlSession = getSession()) {
            try {
                List<TicketWithOneDoctorResponse> TicketWithOneDoctorResponse = getTicketMapper(sqlSession).getAllInfoByTicket(ticketNumber);
                if (TicketWithOneDoctorResponse.size() == 0)
                    throw new HospitalException(HospitalErrorCode.TICKET_NOT_EXISTS);
                return TicketWithOneDoctorResponse;
            } catch (RuntimeException e) {
                LOGGER.error("Can't get all info about appointment by ticket with number {}", ticketNumber);
                e.printStackTrace();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public Ticket getTicket(int doctorId, LocalDate date, LocalTime time) throws HospitalException {
        LOGGER.debug("DAO get ticket by doctor id = {}, date = {} and time = {}", doctorId, date, time);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getTicketMapper(sqlSession).getTicket(doctorId, date, time);
            } catch (RuntimeException e) {
                LOGGER.error("Can't ticket doctor id = {}, date = {} and time = {}", doctorId, date, time);
                e.printStackTrace();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public Ticket getCommissionTicket(int doctorId, LocalDate date, LocalTime time) throws HospitalException {
        LOGGER.debug("DAO get ticket for commission by doctor id = {}, date = {} and time = {}", doctorId, date, time);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getTicketMapper(sqlSession).getCommissionTicket(doctorId, date, time);
            } catch (RuntimeException e) {
                LOGGER.error("DAO get ticket for commission by doctor id = {}, date = {} and time = {}", doctorId, date, time);
                e.printStackTrace();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public void deleteTicket(String ticketNumber) throws HospitalException {
        LOGGER.debug("DAO delete ticket with number {}", ticketNumber);
        try (SqlSession sqlSession = getSession()) {
            try {
                List<Integer> appointmentId = getAppointmentMapper(sqlSession).getAppointmentIds(ticketNumber);
                if (appointmentId.size() == 0) {
                    throw new HospitalException(HospitalErrorCode.TICKET_NOT_EXISTS);
                }
                for (Integer id : appointmentId) {
                    getAppointmentMapper(sqlSession).ridAppointment(id);
                }
                getTicketMapper(sqlSession).delete(ticketNumber);
            } catch (RuntimeException e) {
                LOGGER.error("Can't delete ticket with number {}", ticketNumber);
                sqlSession.rollback();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

}
