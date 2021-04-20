package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Session;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class TestSessionDaoImpl extends TestDaoBase {

    @Test(expectedExceptions = HospitalException.class)
    public void testSaveWithNonExistedUser() throws HospitalException {
        saveSession(666);
    }

    @Test
    public void testGetSessionByUserId() throws HospitalException {
        saveSession(1);
        Session session = sessionDAO.getByUserId(1);
        assertNotNull(session.getUser().getFirstName());
    }

    @Test
    public void testGetSessionByNotRightUserId() throws HospitalException {
        saveSession(1);
        assertNull(sessionDAO.getByUserId(666));
    }

    @Test
    public void testGetSessionById() throws HospitalException {
        String sessionId = UUID.randomUUID().toString();
        Session request = new Session(sessionId, userDAO.getById(1));
        Session session = sessionDAO.save(request);
        Session sessionFromDB = sessionDAO.getById(sessionId);
        assertEquals(session, sessionFromDB);
    }

    @Test(expectedExceptions = HospitalException.class)
    public void testGetSessionNotRightById() throws HospitalException {
        saveSession(1);
        sessionDAO.getById(UUID.randomUUID().toString());
    }

    @Test(expectedExceptions = HospitalException.class)
    public void testDeleteSession() throws HospitalException {
        String sessionId = UUID.randomUUID().toString();
        Session session = saveSessionById(sessionId);
        sessionDAO.delete(session);
        sessionDAO.getById(sessionId);
    }

}
