package net.thumbtack.school.hospital.dao.impl;

import net.thumbtack.school.hospital.dao.CommissionDAO;
import net.thumbtack.school.hospital.exceptions.HospitalErrorCode;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.State;
import net.thumbtack.school.hospital.model.Ticket;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class CommissionDAOImpl extends MyBatisHelper implements CommissionDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommissionDAOImpl.class);

    @Override
    public void insert(int patientId, LocalDate date, LocalTime time, int duration, List<Appointment> appointments, Ticket ticket) throws HospitalException {
        LOGGER.debug("DAO create commission");
        try (SqlSession sqlSession = getSession()) {
            try {
                getTicketMapper(sqlSession).insert(ticket, patientId);
                for (Appointment appointment : appointments) {
                    int res = getAppointmentMapper(sqlSession).update(appointment.getId(), State.COMMISSION);
                    if (res == 0)
                        throw new HospitalException(HospitalErrorCode.BUSY_TICKET);
                    getTicketMapper(sqlSession).insertAppointmentTicket(ticket.getId(), appointment.getId());
                }
                getCommissionMapper(sqlSession).save(patientId, ticket.getId(), date, time, duration);
            } catch (RuntimeException e) {
                LOGGER.error("Can't create commission", e);
                sqlSession.rollback();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

}
