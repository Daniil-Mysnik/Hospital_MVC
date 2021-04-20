package net.thumbtack.school.hospital.dao.impl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import net.thumbtack.school.hospital.dao.PatientDAO;
import net.thumbtack.school.hospital.exceptions.HospitalErrorCode;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Patient;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class PatientDAOImpl extends MyBatisHelper implements PatientDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientDAOImpl.class);

    @Override
    public Patient save(Patient patient) throws HospitalException {
        LOGGER.debug("DAO insert patient {}", patient);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).insert(patient);
                getPatientMapper(sqlSession).insert(patient);
            } catch (RuntimeException e) {
                LOGGER.error("Can't insert patient", e);
                sqlSession.rollback();
                if (e.getCause() instanceof MySQLIntegrityConstraintViolationException) {
                    throw new HospitalException(HospitalErrorCode.BUSY_LOGIN);
                }
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
        return patient;
    }

    @Override
    public Patient getById(int id) throws HospitalException {
        LOGGER.debug("DAO get patient by Id = {}", id);
        try (SqlSession sqlSession = getSession()) {
            try {
                Patient patient = getPatientMapper(sqlSession).getById(id);
                if (patient == null)
                    throw new HospitalException(HospitalErrorCode.PATIENT_NOT_EXIST);
                return patient;
            } catch (RuntimeException e) {
                LOGGER.error("Can't get patient");
                e.printStackTrace();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public void update(Patient patient) throws HospitalException {
        LOGGER.debug("DAO update patient {}", patient);
        try (SqlSession sqlSession = getSession()) {
            try {
                getPatientMapper(sqlSession).update(patient);
            } catch (RuntimeException e) {
                LOGGER.error("Can't update patient");
                sqlSession.rollback();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

    @Override
    public void delete(Patient patient) throws HospitalException {
        LOGGER.debug("DAO delete patient {}", patient);
        try (SqlSession sqlSession = getSession()) {
            try {
                getPatientMapper(sqlSession).delete(patient);
            } catch (RuntimeException e) {
                LOGGER.error("Can't delete patient");
                sqlSession.rollback();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

}
