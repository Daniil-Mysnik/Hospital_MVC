package net.thumbtack.school.hospital.dao.impl;

import net.thumbtack.school.hospital.dao.ScheduleDAO;
import net.thumbtack.school.hospital.exceptions.HospitalErrorCode;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Doctor;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
public class ScheduleDAOImpl extends MyBatisHelper implements ScheduleDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduleDAOImpl.class);

    @Override
    public int getDuration(int doctorId, LocalDate date) throws HospitalException {
        LOGGER.debug("DAO get duration by date {} and doctor with id {}", date, doctorId);
        try (SqlSession sqlSession = getSession()) {
            try {
                Integer duration = getScheduleMapper(sqlSession).getDuration(doctorId, date);
                if (duration == null)
                    throw new HospitalException(HospitalErrorCode.NONWORKING_TIME);
                return duration;
            } catch (RuntimeException e) {
                LOGGER.error("Can't get duration by date {} and doctor with id {}", date, doctorId);
                e.printStackTrace();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public void checkSchedulesByRoom(String room) throws HospitalException {
        LOGGER.debug("DAO check room is free {}", room);
        try (SqlSession sqlSession = getSession()) {
            try {
                List<Long> appointments = getScheduleMapper(sqlSession).getSchedulesByRoom(room);
                if (appointments.size() > 0) {
                    throw new HospitalException(HospitalErrorCode.BUSY_ROOM);
                }
            } catch (RuntimeException e) {
                LOGGER.error("Can't check room is free {}", room);
                sqlSession.rollback();
                throw new HospitalException(HospitalErrorCode.DOCTOR_NOT_EXIST);
            }
        }
    }

    @Override
    public void updateSchedule(Doctor doctor, List<DaySchedule> oldDaySchedule, List<DaySchedule> newDaySchedules) throws HospitalException {
        LOGGER.debug("DAO update schedule of doctor {}", doctor);
        try (SqlSession sqlSession = getSession()) {
            try {
                for (DaySchedule daySchedule : oldDaySchedule) {
                    getScheduleMapper(sqlSession).delete(daySchedule.getId());
                }
                for (DaySchedule daySchedule : newDaySchedules) {
                    getScheduleMapper(sqlSession).save(daySchedule);
                    for (Appointment appointment : daySchedule.getAppointments()) {
                        getAppointmentMapper(sqlSession).save(appointment);
                    }
                }
            } catch (RuntimeException e) {
                LOGGER.error("Can't update schedule of doctor");
                sqlSession.rollback();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

}
