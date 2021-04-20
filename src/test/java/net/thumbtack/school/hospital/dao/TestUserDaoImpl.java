package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.User;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TestUserDaoImpl extends TestDaoBase {

    @Test
    public void testGetUserById() throws HospitalException {
        User user =  userDAO.getById(1);
        assertEquals("Daniil", user.getFirstName());
        assertEquals("Mysnik", user.getLastName());
        assertEquals("Leonidovich", user.getPatronymic());
        assertEquals("admin12345", user.getLogin());
        assertEquals("admin12345", user.getPassword());
    }

    @Test(expectedExceptions = HospitalException.class)
    public void testGetUserByNonExistedId() throws HospitalException {
        userDAO.getById(666);
    }

    @Test
    public void testGetUserByLogin() throws HospitalException {
        User user =  userDAO.getByLogin("admin12345");
        assertEquals("Daniil", user.getFirstName());
        assertEquals("Mysnik", user.getLastName());
        assertEquals("Leonidovich", user.getPatronymic());
        assertEquals("admin12345", user.getLogin());
        assertEquals("admin12345", user.getPassword());
    }

    @Test
    public void testGetUserByNonExistedLogin() throws HospitalException {
        assertNull(userDAO.getByLogin("askrgfhjsaduy"));
    }

    @Test
    public void testGetUserBySessionId() throws HospitalException {
        String sessionId = UUID.randomUUID().toString();
        saveSessionById(sessionId);
        User user = userDAO.getBySessionId(sessionId);
        assertEquals("Daniil", user.getFirstName());
        assertEquals("Mysnik", user.getLastName());
        assertEquals("Leonidovich", user.getPatronymic());
        assertEquals("admin12345", user.getLogin());
        assertEquals("admin12345", user.getPassword());
    }

    @Test(expectedExceptions = HospitalException.class)
    public void testGetUserByNonExistedSessionId() throws HospitalException {
        userDAO.getBySessionId(UUID.randomUUID().toString());
    }

}
