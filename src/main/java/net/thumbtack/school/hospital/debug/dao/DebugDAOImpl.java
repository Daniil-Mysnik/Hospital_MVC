package net.thumbtack.school.hospital.debug.dao;

import net.thumbtack.school.hospital.dao.impl.MyBatisHelper;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DebugDAOImpl extends MyBatisHelper implements DebugDAO {

    private static final Logger LOGGER = LoggerFactory.getLogger(DebugDAOImpl.class);

    public void clear() {
        LOGGER.debug("Clear DB");
        try (SqlSession sqlSession = getSession()) {
            try {
                getUserMapper(sqlSession).deleteAll();
                getSessionMapper(sqlSession).deleteAll();
            } catch (RuntimeException ex) {
                LOGGER.info("Can't clear DB", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

}
