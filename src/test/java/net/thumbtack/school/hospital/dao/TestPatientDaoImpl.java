package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Patient;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import static org.junit.Assert.*;

public class TestPatientDaoImpl extends TestDaoBase {

    @Parameters
    @DataProvider(name = "insert")
    public static Object[][] insertDoctorWithNullParameters() {
        return new Object[][] {
                {null, "Sidorov", "Sergeevich", "sidorov12345", "sidorov12345", "Sidorov@gmail.com", "Gagarina,14", "8-800-555-35-35"},
                {"Vyacheslav", null, "Sergeevich", "sidorov12345", "sidorov12345", "Sidorov@gmail.com", "Gagarina,14", "8-800-555-35-35"},
                {"Vyacheslav", "Sidorov", "Sergeevich", null, "sidorov12345", "Sidorov@gmail.com", "Gagarina,14", "8-800-555-35-35"},
                {"Vyacheslav", "Sidorov", "Sergeevich", "sidorov12345", null, "Sidorov@gmail.com", "Gagarina,14", "8-800-555-35-35"},
                {"Vyacheslav", "Sidorov", "Sergeevich", "sidorov12345", "sidorov12345", null, "Gagarina,14", "8-800-555-35-35"},
                {"Vyacheslav", "Sidorov", "Sergeevich", "sidorov12345", "sidorov12345", "Sidorov@gmail.com", null, "8-800-555-35-35"},
                {"Vyacheslav", "Sidorov", "Sergeevich", "sidorov12345", "sidorov12345", "Sidorov@gmail.com", "Gagarina,14", null}
        };
    }

    @Test(dataProvider = "insert", expectedExceptions = HospitalException.class)
    public void testInsertDoctorWithNullParameters(String firstName, String lastName, String patronymic, String login, String password, String email, String address, String phone) throws HospitalException {
        insertPatient(firstName, lastName, patronymic, login, password, email, address, phone);
    }

    @Test
    public void testInsertPatient() throws HospitalException {
        Patient patient = insertPatient("Vyacheslav", "Sidorov", "Sergeevich", "sidorov12345", "sidorov12345", "Sidorov@gmail.com", "Gagarina,14", "8-800-555-35-35");
        Patient patientFromDB = patientDAO.getById(patient.getId());
        assertEquals("Vyacheslav", patientFromDB.getFirstName());
        assertEquals("Sidorov", patientFromDB.getLastName());
        assertEquals("Sergeevich", patientFromDB.getPatronymic());
        assertEquals("sidorov12345", patientFromDB.getLogin());
        assertEquals("sidorov12345", patientFromDB.getPassword());
        assertEquals("Sidorov@gmail.com", patientFromDB.getEmail());
        assertEquals("Gagarina,14", patientFromDB.getAddress());
        assertEquals("8-800-555-35-35", patientFromDB.getPhone());
    }

    @Test
    public void testInsertPatientWithoutPatronymic() throws HospitalException {
        Patient patient = insertPatient("Vyacheslav", "Sidorov", null, "sidorov12345", "sidorov12345", "Sidorov@gmail.com", "Gagarina,14", "8-800-555-35-35");
        Patient patientFromDB = patientDAO.getById(patient.getId());
        assertEquals(patient, patientFromDB);
    }

    @Test(expectedExceptions = HospitalException.class)
    public void testGetNonexistentPatient() throws HospitalException {
        assertNull(patientDAO.getById(666));
    }

    @Test
    public void testUpdatePatient() throws HospitalException {
        Patient patient = insertPatient("Vyacheslav", "Sidorov", "Sergeevich", "sidorov12345", "sidorov12345", "Sidorov@gmail.com", "Gagarina,14", "8-800-555-35-35");
        Patient patientFromDB = patientDAO.getById(patient.getId());
        assertEquals(patient, patientFromDB);
        patient.setFirstName("Boris");
        patientDAO.update(patient);
        patientFromDB = patientDAO.getById(patient.getId());
        assertEquals(patient, patientFromDB);
        assertEquals("Boris", patientFromDB.getFirstName());
    }

    @Test(expectedExceptions = HospitalException.class)
    public void testUpdatePatientSetNullName() throws HospitalException {
        Patient patient = insertPatient("Vyacheslav", "Sidorov", "Sergeevich", "sidorov12345", "sidorov12345", "Sidorov@gmail.com", "Gagarina,14", "8-800-555-35-35");
        Patient patientFromDB = patientDAO.getById(patient.getId());
        assertEquals(patient, patientFromDB);
        patient.setFirstName(null);
        patientDAO.update(patient);
    }

    @Test(expectedExceptions = HospitalException.class)
    public void testDeleteDoctor() throws HospitalException {
        Patient patient = insertPatient("Vyacheslav", "Sidorov", "Sergeevich", "sidorov12345", "sidorov12345", "Sidorov@gmail.com", "Gagarina,14", "8-800-555-35-35");
        Patient patientFromDB = patientDAO.getById(patient.getId());
        assertEquals(patient, patientFromDB);
        patientDAO.delete(patient);
        patientDAO.getById(patient.getId());
    }

}
