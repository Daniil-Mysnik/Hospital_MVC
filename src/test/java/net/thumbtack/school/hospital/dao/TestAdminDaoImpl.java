package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Admin;
import net.thumbtack.school.hospital.model.Session;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.junit.Assert.*;

public class TestAdminDaoImpl extends TestDaoBase {

    @Parameters
    @DataProvider(name = "insert")
    public static Object[][] insertAdminWithNullParameters() {
        return new Object[][] {
                {null, "Popov", "Alexandrovich", "popov12345", "popov12345", "Web-server"},
                {"Andrew", null, "Alexandrovich", "popov12345", "popov12345", "Web-server"},
                {"Andrew", "Popov", "Alexandrovich", null, "popov12345", "Web-server"},
                {"Andrew", "Popov", "Alexandrovich", "popov12345", null, "Web-server"},
                {"Andrew", "Popov", "Alexandrovich", "popov12345", "popov12345", null}
        };
    }

    @Test(dataProvider = "insert", expectedExceptions = HospitalException.class)
    public void testInsertAdminWithNullParameters(String firstName, String lastName, String patronymic, String login, String password, String position) throws HospitalException {
        insertAdmin(firstName, lastName, patronymic, login, password, position);
    }

    @Test
    public void testInsertAdmin() throws HospitalException {
        Admin admin = insertAdmin("Andrew", "Popov", "Alexandrovich", "popov12345", "popov12345", "Web-server");
        Admin adminFromDB = adminDAO.getById(admin.getId());
        assertEquals("Andrew", adminFromDB.getFirstName());
        assertEquals("Popov", adminFromDB.getLastName());
        assertEquals("Alexandrovich", adminFromDB.getPatronymic());
        assertEquals("popov12345", adminFromDB.getLogin());
        assertEquals("popov12345", adminFromDB.getPassword());
        assertEquals("Web-server", adminFromDB.getPosition());
    }

    @Test
    public void testInsertAdminWithoutPatronymic() throws HospitalException {
        Admin admin = insertAdmin("Andrew", "Popov", null, "popov12345", "popov12345", "Web-server");
        Admin adminFromDB = adminDAO.getById(admin.getId());
        assertEquals(admin, adminFromDB);
    }

    @Test(expectedExceptions = HospitalException.class)
    public void testGetNonexistentAdmin() throws HospitalException {
        assertNull(adminDAO.getById(666));
    }

    @Test
    public void getBySessionId() throws HospitalException {
        Admin admin = insertAdmin("Andrew", "Popov", null, "popov12345", "popov12345", "Web-server");
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId, admin);
        sessionDAO.save(session);
        Admin adminBySession = adminDAO.getBySessionId(sessionId);
        assertEquals(admin, adminBySession);
    }

    @Test
    public void testUpdateAdmin() throws HospitalException {
        Admin admin = insertAdmin("Andrew", "Popov", null, "popov12345", "popov12345", "Web-server");
        Admin adminFromDB = adminDAO.getById(admin.getId());
        assertEquals(admin, adminFromDB);
        admin.setFirstName("Boris");
        adminDAO.update(admin);
        adminFromDB = adminDAO.getById(admin.getId());
        assertEquals(admin, adminFromDB);
        assertEquals("Boris", adminFromDB.getFirstName());
    }

    @Test(expectedExceptions = HospitalException.class)
    public void testUpdateAdminSetNullName() throws HospitalException {
        Admin admin = insertAdmin("Andrew", "Popov", null, "popov12345", "popov12345", "Web-server");
        Admin adminFromDB = adminDAO.getById(admin.getId());
        assertEquals(admin, adminFromDB);
        admin.setFirstName(null);
        adminDAO.update(admin);
    }

    @Test(expectedExceptions = HospitalException.class)
    public void testDeleteAdmin() throws HospitalException {
        Admin admin = insertAdmin("Andrew", "Popov", null, "popov12345", "popov12345", "Web-server");
        Admin adminFromDB = adminDAO.getById(admin.getId());
        assertEquals(admin, adminFromDB);
        adminDAO.delete(admin);
        adminDAO.getById(admin.getId());
    }

}
