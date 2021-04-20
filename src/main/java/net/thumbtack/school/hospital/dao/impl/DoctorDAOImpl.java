package net.thumbtack.school.hospital.dao.impl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import net.thumbtack.school.hospital.dao.DoctorDAO;
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
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class DoctorDAOImpl extends MyBatisHelper implements DoctorDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorDAOImpl.class);

    @Override
    public Doctor save(Doctor doctor) throws HospitalException {
        LOGGER.debug("DAO insert doctor {}", doctor);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).insert(doctor);
                getDoctorMapper(sqlSession).insert(doctor);
                getScheduleMapper(sqlSession).batchInsert(doctor.getDayScheduleList());
                List<Appointment> appointments = doctor.getDayScheduleList().stream()
                        .map(DaySchedule::getAppointments)
                        .flatMap(Collection::stream)
                        .collect(Collectors.toList());
                getAppointmentMapper(sqlSession).batchInsert(appointments);
            } catch (RuntimeException e) {
                LOGGER.error("Can't insert doctor", e);
                sqlSession.rollback();
                if (e.getCause() instanceof MySQLIntegrityConstraintViolationException) {
                    if (e.getCause().getLocalizedMessage().contains("speciality_id")) {
                        throw new HospitalException(HospitalErrorCode.INCORRECT_SPECIALITY);
                    }
                    if (e.getCause().getLocalizedMessage().contains("room_id")) {
                        throw new HospitalException(HospitalErrorCode.WRONG_ROOM);
                    }
                    throw new HospitalException(HospitalErrorCode.BUSY_LOGIN);
                }
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
        return doctor;
    }

    @Override
    public Doctor getById(int id) throws HospitalException {
        LOGGER.debug("DAO get doctor by Id = {}", id);
        try (SqlSession sqlSession = getSession()) {
            try {
                Doctor doctor = getDoctorMapper(sqlSession).getById(id);
                if (doctor == null)
                    throw new HospitalException(HospitalErrorCode.DOCTOR_NOT_EXIST);
                return doctor;
            } catch (RuntimeException e) {
                LOGGER.error("Can't get doctor");
                e.printStackTrace();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public List<Doctor> getBySpeciality(String speciality) throws HospitalException {
        LOGGER.debug("DAO get doctors by speciality = {}", speciality);
        try (SqlSession sqlSession = getSession()) {
            try {
                List<Doctor> doctors = getDoctorMapper(sqlSession).getBySpeciality(speciality);
                if (doctors.isEmpty())
                    throw new HospitalException(HospitalErrorCode.EMPTY_DOCTORS);
                return doctors;
            } catch (RuntimeException e) {
                LOGGER.error("Can't get doctors by speciality");
                e.printStackTrace();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public List<Doctor> getAll() throws HospitalException {
        LOGGER.debug("DAO get all doctors");
        try (SqlSession sqlSession = getSession()) {
            try {
                List<Doctor> doctors = getDoctorMapper(sqlSession).getAll();
                if (doctors.isEmpty())
                    throw new HospitalException(HospitalErrorCode.INCORRECT_SPECIALITY);
                return doctors;
            } catch (RuntimeException e) {
                LOGGER.error("Can't get doctors by speciality");
                e.printStackTrace();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public List<Integer> getIdsBySpeciality(String speciality) throws HospitalException {
        LOGGER.debug("DAO get id's by speciality = {}", speciality);
        try (SqlSession sqlSession = getSession()) {
            try {
                List<Integer> listId = getDoctorMapper(sqlSession).getIdBySpeciality(speciality);
                if (listId.isEmpty())
                    throw new HospitalException(HospitalErrorCode.INCORRECT_SPECIALITY);
                return listId;
            } catch (RuntimeException e) {
                LOGGER.error("Can't get all id's");
                e.printStackTrace();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public List<Integer> getAllIds() throws HospitalException {
        LOGGER.debug("DAO get all id's");
        try (SqlSession sqlSession = getSession()) {
            try {
                List<Integer> listId = getDoctorMapper(sqlSession).getAllId();
                if (listId.isEmpty())
                    throw new HospitalException(HospitalErrorCode.USER_NOT_EXISTS);
                return listId;
            } catch (RuntimeException e) {
                LOGGER.error("Can't get id's by speciality");
                e.printStackTrace();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public List<String> getAllSpecialities() throws HospitalException {
        LOGGER.debug("DAO get all specialities");
        try (SqlSession sqlSession = getSession()) {
            try {
                List<String> specialities = getDoctorMapper(sqlSession).getAllSpecialities();
                if (specialities.isEmpty())
                    throw new HospitalException(HospitalErrorCode.EMPTY_SPECIALITIES);
                return specialities;
            } catch (RuntimeException e) {
                LOGGER.error("Can't get all specialities");
                e.printStackTrace();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public Doctor getRandomBySpeciality(String speciality, LocalDate date, LocalTime time) throws HospitalException {
        LOGGER.debug("DAO get random doctor by speciality {}", speciality);
        try (SqlSession sqlSession = getSession()) {
            try {
                Doctor doctor = getDoctorMapper(sqlSession).getRandomBySpeciality(speciality, date, time);
                if (doctor == null)
                    throw new HospitalException(HospitalErrorCode.INCORRECT_SPECIALITY);
                return doctor;
            } catch (RuntimeException e) {
                LOGGER.error("Can't get random doctor by speciality {}", speciality);
                e.printStackTrace();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public String getRoomByDoctorId(int doctorId) throws HospitalException {
        LOGGER.debug("DAO get room by doctor id {}", doctorId);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getDoctorMapper(sqlSession).getRoomByDoctorId(doctorId);
            } catch (RuntimeException e) {
                LOGGER.error("Can't get room by doctor id {}", doctorId);
                e.printStackTrace();
                throw new HospitalException(HospitalErrorCode.DOCTOR_NOT_EXIST);
            }
        }
    }

    @Override
    public void update(Doctor doctor) throws HospitalException {
        LOGGER.debug("DAO update doctor {}", doctor);
        try (SqlSession sqlSession = getSession()) {
            try {
                getDoctorMapper(sqlSession).update(doctor);
            } catch (RuntimeException e) {
                LOGGER.error("Can't update doctor");
                sqlSession.rollback();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

    @Override
    public void deleteByDate(int doctorId, LocalDate date) throws HospitalException {
        LOGGER.debug("DAO delete doctor with id {}, {}", doctorId, date);
        try (SqlSession sqlSession = getSession()) {
            try {
                getScheduleMapper(sqlSession).deleteByDate(doctorId, date);
            } catch (RuntimeException e) {
                LOGGER.error("Can't delete doctor");
                sqlSession.rollback();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

    @Override
    public void delete(Doctor doctor) throws HospitalException {
        LOGGER.debug("DAO delete doctor {}", doctor);
        try (SqlSession sqlSession = getSession()) {
            try {
                getDoctorMapper(sqlSession).delete(doctor.getId());
            } catch (RuntimeException e) {
                LOGGER.error("Can't delete doctor");
                sqlSession.rollback();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

}
