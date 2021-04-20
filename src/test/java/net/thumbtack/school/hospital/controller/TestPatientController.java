package net.thumbtack.school.hospital.controller;

import com.google.gson.reflect.TypeToken;
import net.thumbtack.school.hospital.dto.request.*;
import net.thumbtack.school.hospital.dto.response.ErrorResponse;
import net.thumbtack.school.hospital.dto.response.PatientResponse;
import net.thumbtack.school.hospital.exceptions.HospitalErrorCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestPatientController extends TestControllerBase {

    @Test
    public void testCreatePatient() {
        CreatePatientRequest request = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        ResponseEntity<PatientResponse> response = regPatient(request);
        assertNotEquals(0, response.getBody().getId());
        assertEquals(request.getFirstName(), response.getBody().getFirstName());
        assertEquals(request.getLastName(), response.getBody().getLastName());
        assertEquals(request.getPatronymic(), response.getBody().getPatronymic());
        assertEquals(request.getEmail(), response.getBody().getEmail());
        assertEquals(request.getAddress(), response.getBody().getAddress());
        assertEquals(request.getPhone(), response.getBody().getPhone());
        assertNotNull(response.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
    }

    @Test
    public void testCreateDuplicateAdmin() {
        CreatePatientRequest request = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        regPatient(request);
        assertHospitalException(request, HospitalErrorCode.BUSY_LOGIN);
    }

    @Test
    public void testCreateAdminWithWrongFirstName() {
        CreatePatientRequest request = new CreatePatientRequest("asdfgsdf", "Попов", "Александрович", "popov1234567", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        assertValidationException(request, "PATTERN", "firstName");
        request.setFirstName(null);
        assertValidationException(request, "NOTNULL", "firstName");
        request.setFirstName("");
        assertValidationException(request, "NOTBLANK", "firstName");
        request.setFirstName(" ");
        assertValidationException(request, "NOTBLANK", "firstName");
        request.setFirstName("      ");
        assertValidationException(request, "NOTBLANK", "firstName");
        request.setFirstName("999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999");
        assertValidationException(request, "MAXLENGTH", "firstName");
        request.setFirstName("Ан-дрей");
        assertEquals(HttpStatus.OK, regPatient(request).getStatusCode());
    }

    @Test
    public void testCreateAdminWithWrongLastName() {
        CreatePatientRequest request = new CreatePatientRequest("Андрей", "sdfgsd", "Александрович", "popov1234567", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        assertValidationException(request, "PATTERN", "lastName");
        request.setLastName(null);
        assertValidationException(request, "NOTNULL", "lastName");
        request.setLastName("");
        assertValidationException(request, "NOTBLANK", "lastName");
        request.setLastName(" ");
        assertValidationException(request, "NOTBLANK", "lastName");
        request.setLastName("      ");
        assertValidationException(request, "NOTBLANK", "lastName");
        request.setLastName("999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999");
        assertValidationException(request, "MAXLENGTH", "lastName");
        request.setLastName("По-пов");
        assertEquals(HttpStatus.OK, regPatient(request).getStatusCode());
    }

    @Test
    public void testCreateAdminWithWrongPatronymic() {
        CreatePatientRequest request = new CreatePatientRequest("Андрей", "Попов", "sdfgsdgf", "popov1234567", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        assertValidationException(request, "BLANKORPATTERN", "patronymic");
        request.setPatronymic("999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999");
        assertValidationException(request, "MAXLENGTH", "patronymic");
        request.setPatronymic("");
        assertEquals(HttpStatus.OK, regPatient(request).getStatusCode());
        clearDB();
        request.setPatronymic("      ");
        assertEquals(HttpStatus.OK, regPatient(request).getStatusCode());
        clearDB();
        request.setPatronymic(null);
        assertEquals(HttpStatus.OK, regPatient(request).getStatusCode());
        clearDB();
        request.setPatronymic("Алек-сандрович");
        assertEquals(HttpStatus.OK, regPatient(request).getStatusCode());
    }

    @Test
    public void testCreateAdminWithWrongLogin() {
        CreatePatientRequest request = new CreatePatientRequest("Андрей", "Попов", "Александрович", "@", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        assertValidationException(request, "PATTERN", "login");
        request.setLogin(null);
        assertValidationException(request, "NOTNULL", "login");
        request.setLogin("");
        assertValidationException(request, "NOTBLANK", "login");
        request.setLogin(" ");
        assertValidationException(request, "NOTBLANK", "login");
        request.setLogin("      ");
        assertValidationException(request, "NOTBLANK", "login");
        request.setLogin("999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999");
        assertValidationException(request, "MAXLENGTH", "login");
    }

    @Test
    public void testCreateAdminWithWrongPassword() {
        CreatePatientRequest request = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "@", "popov@mail.ru", "Gagarina, 14", "88005553535");
        assertValidationException(request, "PASSWORDLENGTH", "password");
        request.setPassword(null);
        assertValidationException(request, "NOTNULL", "password");
        request.setPassword("");
        assertValidationException(request, "NOTBLANK", "password");
        request.setPassword(" ");
        assertValidationException(request, "NOTBLANK", "password");
        request.setPassword("      ");
        assertValidationException(request, "NOTBLANK", "password");
        request.setPassword("999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999");
        assertValidationException(request, "MAXLENGTH", "password");
    }

    @Test
    public void testCreateAdminWithWrongEmail() {
        CreatePatientRequest request = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", null, "Gagarina, 14", "88005553535");
        assertValidationException(request, "NOTNULL", "email");
        request.setEmail("");
        assertValidationException(request, "NOTBLANK", "email");
        request.setEmail(" ");
        assertValidationException(request, "NOTBLANK", "email");
        request.setEmail("      ");
        assertValidationException(request, "NOTBLANK", "email");
        request.setEmail("999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999");
        assertValidationException(request, "MAXLENGTH", "email");
        request.setEmail("alisdfuhasvkh");
        assertValidationException(request, "EMAIL", "email");
    }

    @Test
    public void testCreateAdminWithWrongAddress() {
        CreatePatientRequest request = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "popov@mail.ru", null, "88005553535");
        assertValidationException(request, "NOTNULL", "address");
        request.setAddress("");
        assertValidationException(request, "NOTBLANK", "address");
        request.setAddress(" ");
        assertValidationException(request, "NOTBLANK", "address");
        request.setAddress("      ");
        assertValidationException(request, "NOTBLANK", "address");
    }

    @Test
    public void testCreateAdminWithWrongPhone() {
        CreatePatientRequest request = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "popov@mail.ru", "Gagarina, 14", null);
        assertValidationException(request, "NOTNULL", "phone");
        request.setPhone("");
        assertValidationException(request, "NOTBLANK", "phone");
        request.setPhone(" ");
        assertValidationException(request, "NOTBLANK", "phone");
        request.setPhone("      ");
        assertValidationException(request, "NOTBLANK", "phone");
        request.setPhone("234523");
        assertValidationException(request, "PATTERN", "phone");
        request.setPhone("+58005553535");
        assertValidationException(request, "PATTERN", "phone");
        request.setPhone("+78005553535");
        assertEquals(HttpStatus.OK, regPatient(request).getStatusCode());
        clearDB();
        request.setPhone("+7-800-555-35-35");
        assertEquals(HttpStatus.OK, regPatient(request).getStatusCode());
        clearDB();
        request.setPhone("8-800-555-35-35");
        assertEquals(HttpStatus.OK, regPatient(request).getStatusCode());
        clearDB();
        request.setPhone("88005553535");
        assertEquals(HttpStatus.OK, regPatient(request).getStatusCode());
    }

    @Test
    public void testGetPatientByAdmin() {
        CreatePatientRequest request = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        ResponseEntity<PatientResponse> response = regPatient(request);
        PatientResponse patientInfo = template.exchange(host + port + "/api/patients/" + response.getBody().getId(), HttpMethod.GET, new HttpEntity<>(loginMainAdmin()), PatientResponse.class).getBody();
        assertEquals(request.getFirstName(), patientInfo.getFirstName());
        assertEquals(request.getLastName(), patientInfo.getLastName());
        assertEquals(request.getPatronymic(), patientInfo.getPatronymic());
        assertEquals(request.getEmail(), patientInfo.getEmail());
        assertEquals(request.getAddress(), patientInfo.getAddress());
        assertEquals(request.getPhone(), patientInfo.getPhone());
    }

    @Test
    public void testGetPatientByDoctor() {
        CreatePatientRequest request = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        ResponseEntity<PatientResponse> response = regPatient(request);
        List<String> weekDays = new ArrayList<>();
        weekDays.add("Mon");
        CreateDoctorRequest doctorRequest = new CreateDoctorRequest(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 10),
                new WeekScheduleRequest(LocalTime.of(8, 0), LocalTime.of(10, 0), weekDays), null,
                15, "Андрей", "Попов", "Александрович", "Oculist", "1", "popov12345678", "popov1234567");
        regDoctor(doctorRequest);
        HttpHeaders headers = login(new LoginRequest(doctorRequest.getLogin(), doctorRequest.getPassword()));
        PatientResponse patientInfo = template.exchange(host + port + "/api/patients/" + response.getBody().getId(), HttpMethod.GET, new HttpEntity<>(headers), PatientResponse.class).getBody();
        assertEquals(request.getFirstName(), patientInfo.getFirstName());
        assertEquals(request.getLastName(), patientInfo.getLastName());
        assertEquals(request.getPatronymic(), patientInfo.getPatronymic());
        assertEquals(request.getEmail(), patientInfo.getEmail());
        assertEquals(request.getAddress(), patientInfo.getAddress());
        assertEquals(request.getPhone(), patientInfo.getPhone());
    }

    @Test
    public void testGetPatientNotLogged() {
        CreatePatientRequest request = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        ResponseEntity<PatientResponse> response = regPatient(request);
        try {
            template.exchange(host + port + "/api/patients/" + response.getBody().getId(), HttpMethod.GET, new HttpEntity<>(null), PatientResponse.class);
            fail();
        } catch (HttpClientErrorException e) {
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getErrorCode(), HospitalErrorCode.SESSION_NOT_EXIST.toString());
        }
    }

    @Test
    public void testGetPatientByOtherPatient() {
        CreatePatientRequest request = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        ResponseEntity<PatientResponse> response = regPatient(request);
        CreatePatientRequest request2 = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov12345678", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        regPatient(request2);
        HttpHeaders headers = login(new LoginRequest(request2.getLogin(), request2.getPassword()));
        try {
            template.exchange(host + port + "/api/patients/" + response.getBody().getId(), HttpMethod.GET, new HttpEntity<>(headers), PatientResponse.class);
            fail();
        } catch (HttpClientErrorException e) {
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getErrorCode(), HospitalErrorCode.NOT_ENOUGH_RIGHTS.toString());
        }
    }

    @Test
    public void testUpdatePatient() {
        CreatePatientRequest request = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        ResponseEntity<PatientResponse> response = regPatient(request);
        UpdatePatientRequest updateRequest = new UpdatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "newpopov1234567", "newpopov@mail.ru", "newGagarina, 14", "88005553535");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", response.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
        ResponseEntity<PatientResponse> updatedResponse = updPatient(updateRequest, headers);
        assertEquals(updateRequest.getFirstName(), updatedResponse.getBody().getFirstName());
        assertEquals(updateRequest.getLastName(), updatedResponse.getBody().getLastName());
        assertEquals(updateRequest.getPatronymic(), updatedResponse.getBody().getPatronymic());
        assertEquals(updateRequest.getEmail(), updatedResponse.getBody().getEmail());
        assertEquals(updateRequest.getAddress(), updatedResponse.getBody().getAddress());
        assertEquals(updateRequest.getPhone(), updatedResponse.getBody().getPhone());
    }

    @Test
    public void testUpdatePatientWrongFirstName() {
        CreatePatientRequest request = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        ResponseEntity<PatientResponse> response = regPatient(request);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", response.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
        UpdatePatientRequest updateRequest = new UpdatePatientRequest("@", "Попов", "Александрович", "popov1234567", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        assertUpdateValidationException(updateRequest, headers, "PATTERN", "firstName");
        updateRequest.setFirstName(null);
        assertUpdateValidationException(updateRequest, headers, "NOTNULL", "firstName");
        updateRequest.setFirstName("");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "firstName");
        updateRequest.setFirstName(" ");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "firstName");
        updateRequest.setFirstName("       ");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "firstName");
        updateRequest.setFirstName("99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999");
        assertUpdateValidationException(updateRequest, headers, "MAXLENGTH", "firstName");
    }

    @Test
    public void testUpdatePatientWrongLastName() {
        CreatePatientRequest request = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        ResponseEntity<PatientResponse> response = regPatient(request);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", response.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
        UpdatePatientRequest updateRequest = new UpdatePatientRequest("Андрей", "@", "Александрович", "popov1234567", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        assertUpdateValidationException(updateRequest, headers, "PATTERN", "lastName");
        updateRequest.setLastName(null);
        assertUpdateValidationException(updateRequest, headers, "NOTNULL", "lastName");
        updateRequest.setLastName("");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "lastName");
        updateRequest.setLastName(" ");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "lastName");
        updateRequest.setLastName("       ");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "lastName");
        updateRequest.setLastName("99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999");
        assertUpdateValidationException(updateRequest, headers, "MAXLENGTH", "lastName");
    }

    @Test
    public void testUpdatePatientWrongPatronymic() {
        CreatePatientRequest request = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        ResponseEntity<PatientResponse> response = regPatient(request);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", response.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
        UpdatePatientRequest updateRequest = new UpdatePatientRequest("Андрей", "Попов", "@", "popov1234567", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        assertUpdateValidationException(updateRequest, headers, "BLANKORPATTERN", "patronymic");
        updateRequest.setPatronymic("99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999");
        assertUpdateValidationException(updateRequest, headers, "MAXLENGTH", "patronymic");
        updateRequest.setPatronymic(null);
        assertEquals(HttpStatus.OK, updPatient(updateRequest, headers).getStatusCode());
        updateRequest.setPatronymic("");
        assertEquals(HttpStatus.OK, updPatient(updateRequest, headers).getStatusCode());
        updateRequest.setPatronymic("   ");
        assertEquals(HttpStatus.OK, updPatient(updateRequest, headers).getStatusCode());
        updateRequest.setPatronymic("       ");
        assertEquals(HttpStatus.OK, updPatient(updateRequest, headers).getStatusCode());
    }

    @Test
    public void testUpdatePatientWrongOldPassword() {
        CreatePatientRequest request = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        ResponseEntity<PatientResponse> response = regPatient(request);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", response.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
        UpdatePatientRequest updateRequest = new UpdatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "asdf", "popov@mail.ru", "Gagarina, 14", "88005553535");
        assertUpdateValidationException(updateRequest, headers, "PASSWORDLENGTH", "newPassword");
        updateRequest.setNewPassword(null);
        assertUpdateValidationException(updateRequest, headers, "NOTNULL", "newPassword");
        updateRequest.setNewPassword("");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "newPassword");
        updateRequest.setNewPassword(" ");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "newPassword");
        updateRequest.setNewPassword("       ");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "newPassword");
        updateRequest.setNewPassword("99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999");
        assertUpdateValidationException(updateRequest, headers, "MAXLENGTH", "newPassword");
    }

    @Test
    public void testUpdatePatientWrongNewPassword() {
        CreatePatientRequest request = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        ResponseEntity<PatientResponse> response = regPatient(request);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", response.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
        UpdatePatientRequest updateRequest = new UpdatePatientRequest("Андрей", "Попов", "Александрович", "sadfioegfosduo", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        assertUpdateHospitalException(updateRequest, headers, HospitalErrorCode.WRONG_PASSWORD);
    }

    @Test
    public void testUpdatePatientWrongEmail() {
        CreatePatientRequest request = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        ResponseEntity<PatientResponse> response = regPatient(request);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", response.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
        UpdatePatientRequest updateRequest = new UpdatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "serftg", "Gagarina, 14", "88005553535");
        assertUpdateValidationException(updateRequest, headers, "EMAIL", "email");
        updateRequest.setEmail(null);
        assertUpdateValidationException(updateRequest, headers, "NOTNULL", "email");
        updateRequest.setEmail("");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "email");
        updateRequest.setEmail(" ");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "email");
        updateRequest.setEmail("       ");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "email");
        updateRequest.setEmail("99999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999");
        assertUpdateValidationException(updateRequest, headers, "MAXLENGTH", "email");
    }

    @Test
    public void testUpdatePatientWrongAddress() {
        CreatePatientRequest request = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        ResponseEntity<PatientResponse> response = regPatient(request);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", response.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
        UpdatePatientRequest updateRequest = new UpdatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "popov@mail.ru", null, "88005553535");
        assertUpdateValidationException(updateRequest, headers, "NOTNULL", "address");
        updateRequest.setAddress("");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "address");
        updateRequest.setAddress(" ");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "address");
        updateRequest.setAddress("       ");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "address");
    }

    @Test
    public void testUpdatePatientWrongPhone() {
        CreatePatientRequest request = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        ResponseEntity<PatientResponse> response = regPatient(request);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", response.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
        UpdatePatientRequest updateRequest = new UpdatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "popov@mail.ru", "Gagarina, 14", null);
        assertUpdateValidationException(updateRequest, headers, "NOTNULL", "phone");
        updateRequest.setPhone("");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "phone");
        updateRequest.setPhone(" ");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "phone");
        updateRequest.setPhone("      ");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "phone");
        updateRequest.setPhone("234523");
        assertUpdateValidationException(updateRequest, headers, "PATTERN", "phone");
        updateRequest.setPhone("+58005553535");
        assertUpdateValidationException(updateRequest, headers, "PATTERN", "phone");
        updateRequest.setPhone("+78005553535");
        assertEquals(HttpStatus.OK, updPatient(updateRequest, headers).getStatusCode());
        updateRequest.setPhone("+7-800-555-35-35");
        assertEquals(HttpStatus.OK, updPatient(updateRequest, headers).getStatusCode());
        updateRequest.setPhone("8-800-555-35-35");
        assertEquals(HttpStatus.OK, updPatient(updateRequest, headers).getStatusCode());
        updateRequest.setPhone("88005553535");
        assertEquals(HttpStatus.OK, updPatient(updateRequest, headers).getStatusCode());
    }

    private void assertHospitalException(CreatePatientRequest request, HospitalErrorCode code) {
        try {
            regPatient(request);
            fail();
        } catch (HttpClientErrorException e) {
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getErrorCode(), code.toString());
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getField(), code.getField());
        }
    }

    private void assertValidationException(CreatePatientRequest request, String code, String field) {
        try {
            regPatient(request);
            fail();
        } catch (HttpClientErrorException e) {
            Type ErrorResponseList = new TypeToken<ArrayList<ErrorResponse>>() {}.getType();
            List<ErrorResponse> errors = gson.fromJson(e.getResponseBodyAsString(), ErrorResponseList);
            List<String> errorCodes = errors.stream().map(ErrorResponse::getErrorCode).collect(Collectors.toList());
            assertTrue(errorCodes.contains(code));
            assertEquals(errors.get(0).getField(), field);
        }
    }

    private void assertUpdateHospitalException(UpdatePatientRequest request, HttpHeaders headers, HospitalErrorCode code) {
        try {
            updPatient(request, headers);
            fail();
        } catch (HttpClientErrorException e) {
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getErrorCode(), code.toString());
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getField(), code.getField());
        }
    }

    private void assertUpdateValidationException(UpdatePatientRequest request, HttpHeaders headers, String code, String field) {
        try {
            updPatient(request, headers);
            fail();
        } catch (HttpClientErrorException e) {
            Type ErrorResponseList = new TypeToken<ArrayList<ErrorResponse>>() {}.getType();
            List<ErrorResponse> errors = gson.fromJson(e.getResponseBodyAsString(), ErrorResponseList);
            List<String> errorCodes = errors.stream().map(ErrorResponse::getErrorCode).collect(Collectors.toList());
            assertTrue(errorCodes.contains(code));
            assertEquals(errors.get(0).getField(), field);
        }
    }

}
