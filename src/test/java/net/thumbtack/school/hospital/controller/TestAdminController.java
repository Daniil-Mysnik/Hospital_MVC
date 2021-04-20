package net.thumbtack.school.hospital.controller;

import com.google.gson.reflect.TypeToken;
import net.thumbtack.school.hospital.dto.request.CreateAdminRequest;
import net.thumbtack.school.hospital.dto.request.LoginRequest;
import net.thumbtack.school.hospital.dto.request.UpdateAdminRequest;
import net.thumbtack.school.hospital.dto.response.AdminResponse;
import net.thumbtack.school.hospital.dto.response.ErrorResponse;
import net.thumbtack.school.hospital.exceptions.HospitalErrorCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestAdminController extends TestControllerBase {

    @Test
    public void testCreateAdmin() {
        CreateAdminRequest request = new CreateAdminRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "Web-server");
        ResponseEntity<AdminResponse> response = regAdmin(request);
        assertNotEquals(0, response.getBody().getId());
        assertEquals(request.getFirstName(), response.getBody().getFirstName());
        assertEquals(request.getLastName(), response.getBody().getLastName());
        assertEquals(request.getPatronymic(), response.getBody().getPatronymic());
        assertEquals(request.getPosition(), response.getBody().getPosition());
    }

    @Test
    public void testCreateDuplicateAdmin() {
        CreateAdminRequest request = new CreateAdminRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "Web-server");
        regAdmin(request);
        assertHospitalException(request, HospitalErrorCode.BUSY_LOGIN);
    }

    @Test
    public void testCreateAdminWithWrongFirstName() {
        CreateAdminRequest request = new CreateAdminRequest("asvghk", "Попов", "Александрович", "popov1234567", "popov1234567", "Web-server");
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
        assertEquals(HttpStatus.OK, regAdmin(request).getStatusCode());
    }

    @Test
    public void testCreateAdminWithWrongLastName() {
        CreateAdminRequest request = new CreateAdminRequest("Андрей", "hlkfb", "Александрович", "popov1234567", "popov1234567", "Web-server");
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
        assertEquals(HttpStatus.OK, regAdmin(request).getStatusCode());
    }

    @Test
    public void testCreateAdminWithWrongPatronymic() {
        CreateAdminRequest request = new CreateAdminRequest("Андрей", "Попов", "slidfvb", "popov1234567", "popov1234567", "Web-server");
        assertValidationException(request, "BLANKORPATTERN", "patronymic");
        request.setPatronymic("999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999");
        assertValidationException(request, "MAXLENGTH", "patronymic");
        request.setPatronymic("");
        assertEquals(HttpStatus.OK, regAdmin(request).getStatusCode());
        clearDB();
        request.setPatronymic("      ");
        assertEquals(HttpStatus.OK, regAdmin(request).getStatusCode());
        clearDB();
        request.setPatronymic(null);
        assertEquals(HttpStatus.OK, regAdmin(request).getStatusCode());
        clearDB();
        request.setPatronymic("Алек-сандрович");
        assertEquals(HttpStatus.OK, regAdmin(request).getStatusCode());
    }

    @Test
    public void testCreateAdminWithWrongLogin() {
        CreateAdminRequest request = new CreateAdminRequest("Андрей", "Попов", "Александрович", "@", "popov1234567", "Web-server");
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
        CreateAdminRequest request = new CreateAdminRequest("Андрей", "Попов", "Александрович", "popov1234567", "@", "Web-server");
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
    public void testCreateAdminWithWrongPosition() {
        CreateAdminRequest request = new CreateAdminRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", null);
        assertValidationException(request, "NOTNULL", "position");
        request.setPosition("");
        assertValidationException(request, "NOTBLANK", "position");
        request.setPosition(" ");
        assertValidationException(request, "NOTBLANK", "position");
        request.setPosition("      ");
        assertValidationException(request, "NOTBLANK", "position");
    }

    @Test
    public void testUpdateAdmin() {
        CreateAdminRequest request = new CreateAdminRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "Web-server");
        regAdmin(request);
        HttpHeaders headers = login(new LoginRequest(request.getLogin(), request.getPassword()));
        UpdateAdminRequest updateRequest = new UpdateAdminRequest("аываАндрей", "аывПопов", "аыаАлександрович", "newPosition", "popov1234567", "popov1234567");
        ResponseEntity<AdminResponse> updateResponse = updAdmin(updateRequest, headers);
        assertEquals(updateRequest.getFirstName(), updateResponse.getBody().getFirstName());
        assertEquals(updateRequest.getLastName(), updateResponse.getBody().getLastName());
        assertEquals(updateRequest.getPatronymic(), updateResponse.getBody().getPatronymic());
        assertEquals(updateRequest.getPosition(), updateResponse.getBody().getPosition());
    }

    @Test
    public void testUpdateAdminWrongFirstName() {
        CreateAdminRequest request = new CreateAdminRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "Web-server");
        regAdmin(request);
        HttpHeaders headers = login(new LoginRequest(request.getLogin(), request.getPassword()));
        UpdateAdminRequest updateRequest = new UpdateAdminRequest("@", "Попов", "Александрович", "Web-server", "popov1234567", "popov1234567");
        assertUpdateValidationException(updateRequest, headers, "PATTERN", "firstName");
        updateRequest.setFirstName(null);
        assertUpdateValidationException(updateRequest, headers, "NOTNULL", "firstName");
        updateRequest.setFirstName("");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "firstName");
        updateRequest.setFirstName(" ");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "firstName");
        updateRequest.setFirstName("      ");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "firstName");
        updateRequest.setFirstName("999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999");
        assertUpdateValidationException(updateRequest, headers, "MAXLENGTH", "firstName");
        updateRequest.setFirstName("Ан-дрей");
        assertEquals(HttpStatus.OK, updAdmin(updateRequest, headers).getStatusCode());
    }

    @Test
    public void testUpdateAdminWrongLastName() {
        CreateAdminRequest request = new CreateAdminRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "Web-server");
        regAdmin(request);
        HttpHeaders headers = login(new LoginRequest(request.getLogin(), request.getPassword()));
        UpdateAdminRequest updateRequest = new UpdateAdminRequest("Андрей", "@", "Александрович", "Web-server", "popov1234567", "popov1234567");
        assertUpdateValidationException(updateRequest, headers, "PATTERN", "lastName");
        updateRequest.setLastName(null);
        assertUpdateValidationException(updateRequest, headers, "NOTNULL", "lastName");
        updateRequest.setLastName("");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "lastName");
        updateRequest.setLastName(" ");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "lastName");
        updateRequest.setLastName("      ");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "lastName");
        updateRequest.setLastName("999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999");
        assertUpdateValidationException(updateRequest, headers, "MAXLENGTH", "lastName");
        updateRequest.setLastName("По-пов");
        assertEquals(HttpStatus.OK, updAdmin(updateRequest, headers).getStatusCode());
    }

    @Test
    public void testUpdateAdminWrongPatronymic() {
        CreateAdminRequest request = new CreateAdminRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "Web-server");
        regAdmin(request);
        HttpHeaders headers = login(new LoginRequest(request.getLogin(), request.getPassword()));
        UpdateAdminRequest updateRequest = new UpdateAdminRequest("Андрей", "Попов", "@", "Web-server", "popov1234567", "popov1234567");
        assertUpdateValidationException(updateRequest, headers, "BLANKORPATTERN", "patronymic");
        updateRequest.setPatronymic("999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999");
        assertUpdateValidationException(updateRequest, headers, "MAXLENGTH", "patronymic");
        updateRequest.setPatronymic(null);
        assertEquals(HttpStatus.OK, updAdmin(updateRequest, headers).getStatusCode());
        updateRequest.setPatronymic("");
        assertEquals(HttpStatus.OK, updAdmin(updateRequest, headers).getStatusCode());
        updateRequest.setPatronymic("  ");
        assertEquals(HttpStatus.OK, updAdmin(updateRequest, headers).getStatusCode());
        updateRequest.setPatronymic("      ");
        assertEquals(HttpStatus.OK, updAdmin(updateRequest, headers).getStatusCode());
        updateRequest.setPatronymic("По-пов");
        assertEquals(HttpStatus.OK, updAdmin(updateRequest, headers).getStatusCode());
    }

    @Test
    public void testUpdateAdminWrongOldPassword() {
        CreateAdminRequest request = new CreateAdminRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "Web-server");
        regAdmin(request);
        HttpHeaders headers = login(new LoginRequest(request.getLogin(), request.getPassword()));
        UpdateAdminRequest updateRequest = new UpdateAdminRequest("Андрей", "Попов", "Александрович", "Web-server", "sdflgjkyusdfh", "popov1234567");
        assertUpdateHospitalException(updateRequest, headers, HospitalErrorCode.WRONG_PASSWORD);
    }

    @Test
    public void testUpdateAdminWrongNewPassword() {
        CreateAdminRequest request = new CreateAdminRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "Web-server");
        regAdmin(request);
        HttpHeaders headers = login(new LoginRequest(request.getLogin(), request.getPassword()));
        UpdateAdminRequest updateRequest = new UpdateAdminRequest("Андрей", "Попов", "Александрович", "Web-server", "popov1234567", "sghoui");
        assertUpdateValidationException(updateRequest, headers, "PASSWORDLENGTH", "newPassword");
        updateRequest.setNewPassword(null);
        assertUpdateValidationException(updateRequest, headers, "NOTNULL", "newPassword");
        updateRequest.setNewPassword("");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "newPassword");
        updateRequest.setNewPassword(" ");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "newPassword");
        updateRequest.setNewPassword("      ");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "newPassword");
        updateRequest.setNewPassword("999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999");
        assertUpdateValidationException(updateRequest, headers, "MAXLENGTH", "newPassword");
    }

    @Test
    public void testUpdateAdminWrongPosition() {
        CreateAdminRequest request = new CreateAdminRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "Web-server");
        regAdmin(request);
        HttpHeaders headers = login(new LoginRequest(request.getLogin(), request.getPassword()));
        UpdateAdminRequest updateRequest = new UpdateAdminRequest("Андрей", "Попов", "Александрович", null, "popov1234567", "popov1234567");
        assertUpdateValidationException(updateRequest, headers, "NOTNULL", "position");
        updateRequest.setPosition("");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "position");
        updateRequest.setPosition("  ");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "position");
        updateRequest.setPosition("      ");
        assertUpdateValidationException(updateRequest, headers, "NOTBLANK", "position");
    }

    private void assertHospitalException(CreateAdminRequest request, HospitalErrorCode code) {
        try {
            regAdmin(request);
            fail();
        } catch (HttpClientErrorException e) {
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getErrorCode(), code.toString());
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getField(), code.getField());
        }
    }

    private void assertValidationException(CreateAdminRequest request, String code, String field) {
        try {
            regAdmin(request);
            fail();
        } catch (HttpClientErrorException e) {
            Type ErrorResponseList = new TypeToken<ArrayList<ErrorResponse>>() {}.getType();
            List<ErrorResponse> errors = gson.fromJson(e.getResponseBodyAsString(), ErrorResponseList);
            List<String> errorCodes = errors.stream().map(ErrorResponse::getErrorCode).collect(Collectors.toList());
            assertTrue(errorCodes.contains(code));
            assertEquals(errors.get(0).getField(), field);
        }
    }

    private void assertUpdateHospitalException(UpdateAdminRequest request, HttpHeaders headers,  HospitalErrorCode code) {
        try {
            updAdmin(request, headers);
            fail();
        } catch (HttpClientErrorException e) {
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getErrorCode(), code.toString());
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getField(), code.getField());
        }
    }

    private void assertUpdateValidationException(UpdateAdminRequest request, HttpHeaders headers, String code, String field) {
        try {
            updAdmin(request, headers);
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
