package net.thumbtack.school.hospital.controller;

import net.thumbtack.school.hospital.dto.request.CreateAdminRequest;
import net.thumbtack.school.hospital.dto.request.CreatePatientRequest;
import net.thumbtack.school.hospital.dto.request.LoginRequest;
import net.thumbtack.school.hospital.dto.response.AdminResponse;
import net.thumbtack.school.hospital.dto.response.ErrorResponse;
import net.thumbtack.school.hospital.dto.response.PatientResponse;
import net.thumbtack.school.hospital.exceptions.HospitalErrorCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestSessionController extends TestControllerBase {

    @Test
    public void testCreateSession() {
        CreateAdminRequest request = new CreateAdminRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "Web-server");
        ResponseEntity<AdminResponse> response = regAdmin(request);
        try {
            template.exchange(host + port + "/api/accounts", HttpMethod.GET, new HttpEntity<>(response.getHeaders()), AdminResponse.class).getBody();
            fail();
        } catch (HttpClientErrorException e) {
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getErrorCode(), HospitalErrorCode.SESSION_NOT_EXIST.toString());
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getField(), HospitalErrorCode.SESSION_NOT_EXIST.getField());
        }
        HttpHeaders headers = login(new LoginRequest(request.getLogin(), request.getPassword()));
        assertEquals(HttpStatus.OK, template.exchange(host + port + "/api/accounts", HttpMethod.GET, new HttpEntity<>(headers), AdminResponse.class).getStatusCode());
    }

    @Test
    public void testDeleteSession() {
        CreatePatientRequest request = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        ResponseEntity<PatientResponse> response = regPatient(request);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", response.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
        assertEquals(HttpStatus.OK, template.exchange(host + port + "/api/accounts", HttpMethod.GET, new HttpEntity<>(headers), PatientResponse.class).getStatusCode());
        logout(headers);
        try {
            template.exchange(host + port + "/api/accounts", HttpMethod.GET, new HttpEntity<>(headers), PatientResponse.class).getBody();
            fail();
        } catch (HttpClientErrorException e) {
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getErrorCode(), HospitalErrorCode.SESSION_NOT_EXIST.toString());
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getField(), HospitalErrorCode.SESSION_NOT_EXIST.getField());
        }
    }

}
