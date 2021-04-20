package net.thumbtack.school.hospital.controller;

import net.thumbtack.school.hospital.dto.request.*;
import net.thumbtack.school.hospital.dto.response.AdminResponse;
import net.thumbtack.school.hospital.dto.response.DoctorWithScheduleResponse;
import net.thumbtack.school.hospital.dto.response.PatientResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestAccountController extends TestControllerBase {

    @Test
    public void testGetInfoAdmin() {
        CreateAdminRequest request = new CreateAdminRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "Web-server");
        ResponseEntity<AdminResponse> response = regAdmin(request);
        HttpHeaders headers = login(new LoginRequest(request.getLogin(), request.getPassword()));
        AdminResponse info = template.exchange(host + port + "/api/accounts", HttpMethod.GET, new HttpEntity<>(headers), AdminResponse.class).getBody();
        assertEquals(response.getBody().getId(), info.getId());
        assertEquals(response.getBody().getFirstName(), info.getFirstName());
        assertEquals(response.getBody().getLastName(), info.getLastName());
        assertEquals(response.getBody().getPatronymic(), info.getPatronymic());
        assertEquals(response.getBody().getPosition(), info.getPosition());
    }

    @Test
    public void testGetInfoPatient() {
        CreatePatientRequest request = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov1234567", "popov1234567", "popov@mail.ru", "Gagarina, 14", "88005553535");
        ResponseEntity<PatientResponse> response = regPatient(request);
        HttpHeaders headers = login(new LoginRequest(request.getLogin(), request.getPassword()));
        PatientResponse info = template.exchange(host + port + "/api/accounts", HttpMethod.GET, new HttpEntity<>(headers), PatientResponse.class).getBody();
        assertEquals(response.getBody().getId(), info.getId());
        assertEquals(response.getBody().getFirstName(), info.getFirstName());
        assertEquals(response.getBody().getLastName(), info.getLastName());
        assertEquals(response.getBody().getPatronymic(), info.getPatronymic());
        assertEquals(response.getBody().getAddress(), info.getAddress());
        assertEquals(response.getBody().getEmail(), info.getEmail());
    }

    @Test
    public void testGetInfoDoctor() {
        WeekDaysScheduleRequest scheduleRequest = new WeekDaysScheduleRequest(Arrays.asList(
                new AppointmentRequest("Mon", LocalTime.of(8, 0), LocalTime.of(10, 0)),
                new AppointmentRequest("Wed", LocalTime.of(9, 0), LocalTime.of(12, 0))));
        CreateDoctorRequest request = new CreateDoctorRequest(
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 10),
                null,
                scheduleRequest,
                15,
                "Андрей",
                "Попов",
                "Александрович",
                "Oculist",
                "1",
                "popov1234567",
                "popov1234567");
        ResponseEntity<DoctorWithScheduleResponse> response = regDoctor(request);
        HttpHeaders headers = login(new LoginRequest(request.getLogin(), request.getPassword()));
        DoctorWithScheduleResponse info = template.exchange(host + port + "/api/accounts", HttpMethod.GET, new HttpEntity<>(headers), DoctorWithScheduleResponse.class).getBody();
        assertEquals(response.getBody().getFirstName(), info.getFirstName());
        assertEquals(response.getBody().getLastName(), info.getLastName());
        assertEquals(response.getBody().getPatronymic(), info.getPatronymic());
        assertEquals(3, info.getSchedule().size());
    }

}
