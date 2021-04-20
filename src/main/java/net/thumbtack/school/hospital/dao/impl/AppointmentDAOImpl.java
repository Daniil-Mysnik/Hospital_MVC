package net.thumbtack.school.hospital.dao.impl;

import net.thumbtack.school.hospital.dao.AppointmentDAO;
import net.thumbtack.school.hospital.exceptions.HospitalErrorCode;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.State;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class AppointmentDAOImpl extends MyBatisHelper implements AppointmentDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppointmentDAOImpl.class);

    @Override
    public Appointment getAppointmentByTime(int doctorId, LocalDate date, LocalTime time) throws HospitalException {
        LOGGER.debug("DAO get appointment by date {} and time {}", date, time);
        try (SqlSession sqlSession = getSession()) {
            try {
                Appointment appointment = getAppointmentMapper(sqlSession).getAppointmentByTime(doctorId, date, time);
                if (appointment == null)
                    throw new HospitalException(HospitalErrorCode.NONWORKING_TIME);
                return appointment;
            } catch (RuntimeException e) {
                LOGGER.error("Can't get schedule by date {} and time {}", date, time);
                e.printStackTrace();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public List<Appointment> getAppointmentsForCommission(LocalDate date, int doctorId, LocalTime timeStart, LocalTime timeEnd) throws HospitalException {
        LOGGER.debug("DAO get appointments for commission by doctor id {}", doctorId);
        try (SqlSession sqlSession = getSession()) {
            try {
                List<Appointment> appointments = getAppointmentMapper(sqlSession).getAppointmentsForCommission(date, doctorId, timeStart, timeEnd);
                if (appointments.size() == 0) {
                    throw new HospitalException(HospitalErrorCode.NONWORKING_TIME);
                }
                return appointments;
            } catch (RuntimeException e) {
                LOGGER.error("Can't get appointments for commission by doctor id {}", doctorId);
                sqlSession.rollback();
                throw new HospitalException(HospitalErrorCode.DOCTOR_NOT_EXIST);
            }
        }
    }

    @Override
    public void updateAppointment(int appointmentId, State state) throws HospitalException {
        LOGGER.debug("DAO update appointment with id {}", appointmentId);
        try (SqlSession sqlSession = getSession()) {
            try {
                int res = getAppointmentMapper(sqlSession).update(appointmentId, state);
                if (res == 0) {
                    throw new HospitalException(HospitalErrorCode.BUSY_TICKET);
                }
            } catch (RuntimeException e) {
                LOGGER.error("Can't update appointment with id {}", appointmentId);
                sqlSession.rollback();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

    @Override
    public void updateAppointments(List<Appointment> appointments, State state) throws HospitalException {
        LOGGER.debug("DAO update appointments for commission");
        try (SqlSession sqlSession = getSession()) {
            try {
                for (Appointment appointment : appointments) {
                    getAppointmentMapper(sqlSession).update(appointment.getId(), state);
                }
            } catch (RuntimeException e) {
                LOGGER.error("Can't update appointments for commission");
                sqlSession.rollback();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

}
