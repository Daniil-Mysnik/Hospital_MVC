package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Doctor;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestDoctorDaoImpl extends TestDaoBase {

    @Parameters
    @DataProvider(name = "insert")
    public static Object[][] insertDoctorWithNullParameters() {
        return new Object[][] {
                {null, "Ivanov", "Evgenievich", "ivanov12345", "ivanov12345", "Oculist", "15a"},
                {"Alex", null, "Evgenievich", "ivanov12345", "ivanov12345", "Oculist", "15a"},
                {"Alex", "Ivanov", "Evgenievich", null, "ivanov12345", "Oculist", "15a"},
                {"Alex", "Ivanov", "Evgenievich", "ivanov12345", null, "Oculist", "15a"},
                {"Alex", "Ivanov", "Evgenievich", "ivanov12345", "ivanov12345", null, "15a"},
                {"Alex", "Ivanov", "Evgenievich", "ivanov12345", "ivanov12345", "Oculist", null}
        };
    }

    @Test(dataProvider = "insert", expectedExceptions = HospitalException.class)
    public void testInsertDoctorWithNullParameters(String firstName, String lastName, String patronymic, String login, String password, String speciality, String room) throws HospitalException {
        insertDoctor(firstName, lastName, patronymic, login, password, speciality, room);
    }

    @Test
    public void testInsertDoctor() throws HospitalException {
        Doctor doctor = insertDoctor("Alex", "Ivanov", "Evgenievich", "ivanov12345", "ivanov12345", "Oculist", "15a");
        Doctor doctorFromDB = doctorDAO.getById(doctor.getId());
        assertEquals("Alex", doctorFromDB.getFirstName());
        assertEquals("Ivanov", doctorFromDB.getLastName());
        assertEquals("Evgenievich", doctorFromDB.getPatronymic());
        assertEquals("ivanov12345", doctorFromDB.getLogin());
        assertEquals("ivanov12345", doctorFromDB.getPassword());
        assertEquals("Oculist", doctorFromDB.getSpeciality());
        assertEquals("15a", doctorFromDB.getRoom());
    }

    @Test
    public void testInsertDoctorWithoutPatronymic() throws HospitalException {
        Doctor doctor = insertDoctor("Alex", "Ivanov", null, "ivanov12345", "ivanov12345", "Oculist", "15a");
        Doctor doctorFromDB = doctorDAO.getById(doctor.getId());
        assertEquals(doctor, doctorFromDB);
    }

    @Test(expectedExceptions = HospitalException.class)
    public void testGetNonexistentDoctor() throws HospitalException {
        assertNull(doctorDAO.getById(666));
    }

    @Test
    public void testGetBySpeciality() throws HospitalException {
        Doctor doctor1 = insertDoctor("Alex", "Ivanov", "Evgenievich", "1ivanov12345", "ivanov12345", "Oculist", "15a");
        Doctor doctor2 = insertDoctor("Alex", "Ivanov", "Evgenievich", "2ivanov12345", "ivanov12345", "Oculist", "15a");
        Doctor doctor3 = insertDoctor("Alex", "Ivanov", "Evgenievich", "3ivanov12345", "ivanov12345", "Therapist", "1");
        Doctor doctor4 = insertDoctor("Alex", "Ivanov", "Evgenievich", "4ivanov12345", "ivanov12345", "Therapist", "1");
        List<Integer> ids = doctorDAO.getIdsBySpeciality("Oculist");
        List<Doctor> doctors = doctorDAO.getBySpeciality("Therapist");
        assertEquals(2, ids.size());
        assertEquals(2, doctors.size());
        assertTrue(ids.contains(doctor1.getId()));
        assertTrue(ids.contains(doctor2.getId()));
        assertTrue(doctors.contains(doctor3));
        assertTrue(doctors.contains(doctor4));
    }

    @Test
    public void testGetAllIds() throws HospitalException {
        Doctor doctor1 = insertDoctor("Alex", "Ivanov", "Evgenievich", "1ivanov12345", "ivanov12345", "Oculist", "15a");
        Doctor doctor2 = insertDoctor("Alex", "Ivanov", "Evgenievich", "2ivanov12345", "ivanov12345", "Oculist", "15a");
        Doctor doctor3 = insertDoctor("Alex", "Ivanov", "Evgenievich", "3ivanov12345", "ivanov12345", "Therapist", "1");
        List<Integer> ids = doctorDAO.getAllIds();
        assertEquals(3, ids.size());
        assertTrue(ids.contains(doctor1.getId()));
        assertTrue(ids.contains(doctor2.getId()));
        assertTrue(ids.contains(doctor3.getId()));
    }

    @Test
    public void testUpdateDoctor() throws HospitalException {
        Doctor doctor = insertDoctor("Alex", "Ivanov", "Evgenievich", "ivanov12345", "ivanov12345", "Oculist", "15a");
        Doctor doctorFromDB = doctorDAO.getById(doctor.getId());
        assertEquals(doctor, doctorFromDB);
        doctor.setFirstName("Boris");
        doctorDAO.update(doctor);
        doctorFromDB = doctorDAO.getById(doctor.getId());
        assertEquals(doctor, doctorFromDB);
        assertEquals("Boris", doctorFromDB.getFirstName());
    }

    @Test(expectedExceptions = HospitalException.class)
    public void testUpdateDoctorSetNullName() throws HospitalException {
        Doctor doctor = insertDoctor("Alex", "Ivanov", "Evgenievich", "ivanov12345", "ivanov12345", "Oculist", "15a");
        Doctor doctorFromDB = doctorDAO.getById(doctor.getId());
        assertEquals(doctor, doctorFromDB);
        doctor.setFirstName(null);
        doctorDAO.update(doctor);
    }

    @Test(expectedExceptions = HospitalException.class)
    public void testDeleteDoctor() throws HospitalException {
        Doctor doctor = insertDoctor("Alex", "Ivanov", "Evgenievich", "ivanov12345", "ivanov12345", "Oculist", "15a");
        Doctor doctorFromDB = doctorDAO.getById(doctor.getId());
        assertEquals(doctor, doctorFromDB);
        doctorDAO.delete(doctor);
        doctorDAO.getById(doctor.getId());
    }


}
