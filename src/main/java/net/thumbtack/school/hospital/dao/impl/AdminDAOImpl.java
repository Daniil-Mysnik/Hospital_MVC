package net.thumbtack.school.hospital.dao.impl;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import net.thumbtack.school.hospital.dao.AdminDAO;
import net.thumbtack.school.hospital.exceptions.HospitalErrorCode;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Admin;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AdminDAOImpl extends MyBatisHelper implements AdminDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminDAOImpl.class);

    @Override
    public Admin save(Admin admin) throws HospitalException {
        LOGGER.debug("DAO insert admin {}", admin);
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).insert(admin);
                getAdminMapper(sqlSession).insert(admin);
            } catch (RuntimeException e) {
                LOGGER.error("Can't insert admin", e);
                sqlSession.rollback();
                if (e.getCause() instanceof MySQLIntegrityConstraintViolationException) {
                    throw new HospitalException(HospitalErrorCode.BUSY_LOGIN);
                }
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
        return admin;
    }

    @Override
    public Admin getById(int id) throws HospitalException {
        LOGGER.debug("DAO get admin by Id = {}", id);
        try (SqlSession sqlSession = getSession()) {
            try {
                Admin admin = getAdminMapper(sqlSession).getById(id);
                if (admin == null)
                    throw new HospitalException(HospitalErrorCode.USER_NOT_EXISTS);
                return admin;
            } catch (RuntimeException e) {
                LOGGER.error("Can't get admin");
                e.printStackTrace();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public Admin getBySessionId(String sessionId) throws HospitalException {
        LOGGER.debug("DAO get admin by SessionId = {}", sessionId);
        try (SqlSession sqlSession = getSession()) {
            try {
                Admin admin = getAdminMapper(sqlSession).getBySessionId(sessionId);
                if (admin == null)
                    throw new HospitalException(HospitalErrorCode.USER_NOT_EXISTS);
                return admin;
            } catch (RuntimeException e) {
                LOGGER.error("Can't get admin by sessionId");
                e.printStackTrace();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public void update(Admin admin) throws HospitalException {
        LOGGER.debug("DAO update admin {}", admin);
        try (SqlSession sqlSession = getSession()) {
            try {
                getAdminMapper(sqlSession).update(admin);
            } catch (RuntimeException e) {
                LOGGER.error("Can't update admin");
                sqlSession.rollback();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

    @Override
    public void delete(Admin admin) throws HospitalException {
        LOGGER.debug("DAO delete admin {}", admin);
        try (SqlSession sqlSession = getSession()) {
            try {
                getAdminMapper(sqlSession).delete(admin);
            } catch (RuntimeException e) {
                LOGGER.error("Can't delete admin");
                sqlSession.rollback();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

}
