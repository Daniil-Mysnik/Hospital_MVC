package net.thumbtack.school.hospital.dao.impl;

import net.thumbtack.school.hospital.dao.UserDAO;
import net.thumbtack.school.hospital.exceptions.HospitalErrorCode;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.User;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class UserDAOImpl extends MyBatisHelper implements UserDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserDAOImpl.class);

    @Override
    public User getById(int id) throws HospitalException {
        LOGGER.debug("DAO get user by Id = {}", id);
        try (SqlSession sqlSession = getSession()) {
            try {
                User user = getUserMapper(sqlSession).getById(id);
                if (user == null)
                    throw new HospitalException(HospitalErrorCode.USER_NOT_EXISTS);
                return user;
            } catch (RuntimeException e) {
                LOGGER.error("Can't get user");
                e.printStackTrace();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public User getByLogin(String login) throws HospitalException {
        LOGGER.debug("DAO get user by login = {}", login);
        try (SqlSession sqlSession = getSession()) {
            try {
                return getUserMapper(sqlSession).getByLogin(login);
            } catch (RuntimeException e) {
                LOGGER.error("Can't get user");
                e.printStackTrace();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
        }
    }

    @Override
    public User getBySessionId(String id) throws HospitalException {
        LOGGER.debug("DAO get user by sessionId = {}", id);
        try (SqlSession sqlSession = getSession()) {
            try {
                User user = getUserMapper(sqlSession).getBySessionId(id);
                if (user == null)
                    throw new HospitalException(HospitalErrorCode.USER_NOT_EXISTS);
                return user;
            } catch (RuntimeException e) {
                LOGGER.error("Can't get user by sessionId");
                e.printStackTrace();
                throw new HospitalException(HospitalErrorCode.DATABASE_ERROR);
            }
        }
    }

}
