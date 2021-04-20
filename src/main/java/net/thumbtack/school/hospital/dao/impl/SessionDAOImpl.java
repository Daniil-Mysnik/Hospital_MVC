package net.thumbtack.school.hospital.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.thumbtack.school.hospital.dao.SessionDAO;
import net.thumbtack.school.hospital.exceptions.HospitalErrorCode;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Session;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

@Component
public class SessionDAOImpl extends MyBatisHelper implements SessionDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(SessionDAOImpl.class);

    @Override
    public Session save(Session session) throws HospitalException {
        LOGGER.debug("DAO create session {}", session);
        try (SqlSession sqlSession = getSession()) {
            try {
                getSessionMapper(sqlSession).insert(session);
            } catch (RuntimeException e) {
                LOGGER.error("Can't create session", e);
                sqlSession.rollback();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
        return session;
    }

    @Override
    public Session getById(String id) throws HospitalException {
        LOGGER.debug("DAO get session by Id = {}", id);
        try (SqlSession sqlSession = getSession()) {
            try {
                Session session = getSessionMapper(sqlSession).getById(id);
                if (session == null)
                    throw new HospitalException(HospitalErrorCode.SESSION_NOT_EXIST);
                return session;
            } catch (RuntimeException e) {
                LOGGER.error("Can't get session by id = {}", id);
                e.printStackTrace();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public Session getByUserId(int userId) throws HospitalException {
        LOGGER.debug("DAO get session by user id = {}", userId);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getSessionMapper(sqlSession).getByUserId(userId);
            } catch (RuntimeException e) {
                LOGGER.error("Can't get session by userId = {}", userId);
                e.printStackTrace();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public void delete(Session session) throws HospitalException {
        LOGGER.debug("DAO delete session {}", session);
        try (SqlSession sqlSession = getSession()) {
            try {
                getSessionMapper(sqlSession).delete(session);
            } catch (RuntimeException e) {
                LOGGER.error("Can't delete session");
                sqlSession.rollback();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
            sqlSession.commit();
        }
    }

}
