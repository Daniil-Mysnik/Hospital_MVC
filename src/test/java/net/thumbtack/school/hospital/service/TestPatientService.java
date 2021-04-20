package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.converter.PatientConverter;
import net.thumbtack.school.hospital.dto.request.CreatePatientRequest;
import net.thumbtack.school.hospital.dto.request.UpdatePatientRequest;
import net.thumbtack.school.hospital.dto.response.PatientResponse;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.validator.UserValidator;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

public class TestPatientService extends TestServiceBase {
    private PatientService patientService = new PatientService(userDAO, patientDAO, new PatientConverter(), new UserValidator(userDAO));

    @Before
    public void initial() throws HospitalException {
        when(userDAO.getBySessionId(anyString())).thenReturn(patient);
        when(patientDAO.save(any(Patient.class))).thenReturn(patient);
    }

    @Test
    public void testCreatePatient() throws HospitalException {
        CreatePatientRequest request = new CreatePatientRequest("Vyacheslav", "Sidorov", null, "sidorov12345", "sidorov12345", "Sidorov@gmail.com", "Gagarina,14", "8-800-555-35-35");
        PatientResponse response = patientService.create(request);
        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getPatronymic(), response.getPatronymic());
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(request.getAddress(), response.getAddress());
        assertEquals(request.getPhone(), response.getPhone());
    }

    @Test(expected = HospitalException.class)
    public void testGetPatientByPatient() throws HospitalException {
        patientService.get(UUID.randomUUID().toString(), 1);
    }

    @Test
    public void testUpdatePatient() throws HospitalException {
        UpdatePatientRequest request = new UpdatePatientRequest("Vyacheslav", "Sidorov", null, "sidorov12345", "sidorov12345", "Sidorov@gmail.com", "Gagarina,14", "8-800-555-35-35");
        when(userDAO.getBySessionId(anyString())).thenReturn(patient);
        when(patientDAO.getById(anyInt())).thenReturn(patient);
        PatientResponse response = patientService.update(UUID.randomUUID().toString(), request);
        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getPatronymic(), response.getPatronymic());
        assertEquals(request.getEmail(), response.getEmail());
        assertEquals(request.getAddress(), response.getAddress());
        assertEquals(request.getPhone(), response.getPhone());
    }

    @Test(expected = HospitalException.class)
    public void testUpdatePatientWithNotMatchPass() throws HospitalException {
        UpdatePatientRequest request = new UpdatePatientRequest("Vyacheslav", "Sidorov", null, "naousdyf!f804", "sidorov12345", "Sidorov@gmail.com", "Gagarina,14", "8-800-555-35-35");
        when(userDAO.getBySessionId(anyString())).thenReturn(patient);
        patientService.update(UUID.randomUUID().toString(), request);
    }

}
