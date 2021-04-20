package net.thumbtack.school.hospital.dao.impl;

import net.thumbtack.school.hospital.mapper.*;
import net.thumbtack.school.hospital.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

public class MyBatisHelper {

    protected SqlSession getSession() {
        return MyBatisUtils.getSqlSessionFactory().openSession();
    }

    protected UserMapper getUserMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(UserMapper.class);
    }

    protected AdminMapper getAdminMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(AdminMapper.class);
    }

    protected PatientMapper getPatientMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(PatientMapper.class);
    }

    protected DoctorMapper getDoctorMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(DoctorMapper.class);
    }

    protected SessionMapper getSessionMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(SessionMapper.class);
    }

    protected ScheduleMapper getScheduleMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(ScheduleMapper.class);
    }

    protected AppointmentMapper getAppointmentMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(AppointmentMapper.class);
    }

    protected TicketMapper getTicketMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(TicketMapper.class);
    }

    protected CommissionMapper getCommissionMapper(SqlSession sqlSession) {
        return sqlSession.getMapper(CommissionMapper.class);
    }

}
