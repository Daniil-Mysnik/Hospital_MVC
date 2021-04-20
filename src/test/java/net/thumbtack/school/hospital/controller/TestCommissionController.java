package net.thumbtack.school.hospital.controller;

import com.google.gson.reflect.TypeToken;
import net.thumbtack.school.hospital.dto.request.*;
import net.thumbtack.school.hospital.dto.response.*;
import net.thumbtack.school.hospital.exceptions.HospitalErrorCode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestCommissionController extends TestControllerBase {

    @Test
    public void testCreateCommission() {
        WeekScheduleRequest scheduleRequest = new WeekScheduleRequest(LocalTime.of(8, 0), LocalTime.of(10, 0), Arrays.asList("Mon", "Wed"));
        CreateDoctorRequest doctorRequest = new CreateDoctorRequest(
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 10),
                scheduleRequest,
                null,
                15,
                "Андрей",
                "Попов",
                "Александрович",
                "Oculist",
                "15a",
                "popov1234567",
                "popov1234567");
        regDoctor(doctorRequest).getBody();
        CreatePatientRequest patientRequest = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov12345678", "popov12345678", "popov@mail.ru", "Gagarina, 14", "88005553535");
        ResponseEntity<PatientResponse> patientResponse = regPatient(patientRequest);
        HttpHeaders patientHeaders = login(new LoginRequest(patientRequest.getLogin(), patientRequest.getPassword()));
        try {
            getTickets(patientHeaders);
            fail();
        } catch (HttpClientErrorException e) {
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getErrorCode(), HospitalErrorCode.PATIENT_HAS_NO_TICKET.toString());
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getField(), HospitalErrorCode.PATIENT_HAS_NO_TICKET.getField());
        }
        HttpHeaders doctorHeaders = login(new LoginRequest(doctorRequest.getLogin(), doctorRequest.getPassword()));
        CommissionRequest commissionRequest = new CommissionRequest(patientResponse.getBody().getId(), new ArrayList<>(), doctorRequest.getRoom(), LocalDate.of(2022, 1, 3), LocalTime.of(8, 0), 40);
        CommissionResponse commissionResponse = createCommission(commissionRequest, doctorHeaders).getBody();
        assertEquals(commissionResponse.getPatientId(), commissionRequest.getPatientId());
        assertEquals(commissionResponse.getDate(), commissionRequest.getDate());
        assertEquals(commissionResponse.getRoom(), commissionRequest.getRoom());
        assertEquals(commissionResponse.getDuration(), commissionRequest.getDuration());
        assertEquals(commissionResponse.getTime(), commissionRequest.getTime());
        assertEquals(1, commissionResponse.getDoctorIds().size());
        TicketListResponse tickets = getTickets(patientHeaders).getBody();
        assertEquals(1, tickets.getTickets().size());
    }

    @Test
    public void testCreateCommissionNotExistedPatient() {
        WeekScheduleRequest scheduleRequest = new WeekScheduleRequest(LocalTime.of(8, 0), LocalTime.of(10, 0), Arrays.asList("Mon", "Wed"));
        CreateDoctorRequest doctorRequest = new CreateDoctorRequest(
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 10),
                scheduleRequest,
                null,
                15,
                "Андрей",
                "Попов",
                "Александрович",
                "Oculist",
                "15a",
                "popov1234567",
                "popov1234567");
        regDoctor(doctorRequest).getBody();
        HttpHeaders doctorHeaders = login(new LoginRequest(doctorRequest.getLogin(), doctorRequest.getPassword()));
        CommissionRequest commissionRequest = new CommissionRequest(666, new ArrayList<>(), doctorRequest.getRoom(), LocalDate.of(2022, 1, 3), LocalTime.of(8, 0), 40);
        assertCreateHospitalException(commissionRequest, doctorHeaders, HospitalErrorCode.PATIENT_NOT_EXIST);
    }

    @Test
    public void testCreateCommissionWithoutDoctors() {
        WeekScheduleRequest scheduleRequest = new WeekScheduleRequest(LocalTime.of(8, 0), LocalTime.of(10, 0), Arrays.asList("Mon", "Wed"));
        CreateDoctorRequest doctorRequest = new CreateDoctorRequest(
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 10),
                scheduleRequest,
                null,
                15,
                "Андрей",
                "Попов",
                "Александрович",
                "Oculist",
                "15a",
                "popov1234567",
                "popov1234567");
        regDoctor(doctorRequest).getBody();
        CreatePatientRequest patientRequest = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov12345678", "popov12345678", "popov@mail.ru", "Gagarina, 14", "88005553535");
        ResponseEntity<PatientResponse> patientResponse = regPatient(patientRequest);
        HttpHeaders doctorHeaders = login(new LoginRequest(doctorRequest.getLogin(), doctorRequest.getPassword()));
        CommissionRequest commissionRequest = new CommissionRequest(patientResponse.getBody().getId(), null, doctorRequest.getRoom(), LocalDate.of(2022, 1, 3), LocalTime.of(8, 0), 40);
        assertCreateValidationException(commissionRequest, doctorHeaders, "NOTNULL", "doctorIds");
    }

    @Test
    public void testCreateCommissionWrongRoom() {
        WeekScheduleRequest scheduleRequest = new WeekScheduleRequest(LocalTime.of(8, 0), LocalTime.of(10, 0), Arrays.asList("Mon", "Wed"));
        CreateDoctorRequest doctorRequest = new CreateDoctorRequest(
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 10),
                scheduleRequest,
                null,
                15,
                "Андрей",
                "Попов",
                "Александрович",
                "Oculist",
                "15a",
                "popov1234567",
                "popov1234567");
        regDoctor(doctorRequest).getBody();
        CreatePatientRequest patientRequest = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov12345678", "popov12345678", "popov@mail.ru", "Gagarina, 14", "88005553535");
        ResponseEntity<PatientResponse> patientResponse = regPatient(patientRequest);
        HttpHeaders doctorHeaders = login(new LoginRequest(doctorRequest.getLogin(), doctorRequest.getPassword()));
        CommissionRequest commissionRequest = new CommissionRequest(patientResponse.getBody().getId(), new ArrayList<>(), "2", LocalDate.of(2022, 1, 3), LocalTime.of(8, 0), 40);
        assertCreateHospitalException(commissionRequest, doctorHeaders, HospitalErrorCode.WRONG_ROOM);
        commissionRequest.setRoom(null);
        assertCreateValidationException(commissionRequest, doctorHeaders, "NOTNULL", "room");
        commissionRequest.setRoom("");
        assertCreateValidationException(commissionRequest, doctorHeaders, "NOTBLANK", "room");
        commissionRequest.setRoom(" ");
        assertCreateValidationException(commissionRequest, doctorHeaders, "NOTBLANK", "room");
        commissionRequest.setRoom("     ");
        assertCreateValidationException(commissionRequest, doctorHeaders, "NOTBLANK", "room");
    }

    @Test
    public void testCreateCommissionWrongDate() {
        WeekScheduleRequest scheduleRequest = new WeekScheduleRequest(LocalTime.of(8, 0), LocalTime.of(10, 0), Arrays.asList("Mon", "Wed"));
        CreateDoctorRequest doctorRequest = new CreateDoctorRequest(
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 10),
                scheduleRequest,
                null,
                15,
                "Андрей",
                "Попов",
                "Александрович",
                "Oculist",
                "15a",
                "popov1234567",
                "popov1234567");
        regDoctor(doctorRequest).getBody();
        CreatePatientRequest patientRequest = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov12345678", "popov12345678", "popov@mail.ru", "Gagarina, 14", "88005553535");
        ResponseEntity<PatientResponse> patientResponse = regPatient(patientRequest);
        HttpHeaders doctorHeaders = login(new LoginRequest(doctorRequest.getLogin(), doctorRequest.getPassword()));
        CommissionRequest commissionRequest = new CommissionRequest(patientResponse.getBody().getId(), new ArrayList<>(), "15a", LocalDate.of(1950, 1, 30), LocalTime.of(8, 0), 40);
        assertCreateValidationException(commissionRequest, doctorHeaders, "FUTURE", "date");
    }

    @Test
    public void testCreateCommissionNonWorkingTime() {
        WeekScheduleRequest scheduleRequest = new WeekScheduleRequest(LocalTime.of(8, 0), LocalTime.of(10, 0), Arrays.asList("Mon", "Wed"));
        CreateDoctorRequest doctorRequest = new CreateDoctorRequest(
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 10),
                scheduleRequest,
                null,
                15,
                "Андрей",
                "Попов",
                "Александрович",
                "Oculist",
                "15a",
                "popov1234567",
                "popov1234567");
        regDoctor(doctorRequest).getBody();
        CreatePatientRequest patientRequest = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov12345678", "popov12345678", "popov@mail.ru", "Gagarina, 14", "88005553535");
        ResponseEntity<PatientResponse> patientResponse = regPatient(patientRequest);
        HttpHeaders doctorHeaders = login(new LoginRequest(doctorRequest.getLogin(), doctorRequest.getPassword()));
        CommissionRequest commissionRequest = new CommissionRequest(patientResponse.getBody().getId(), new ArrayList<>(), "15a", LocalDate.of(2022, 1, 30), LocalTime.of(8, 0), 40);
        assertCreateHospitalException(commissionRequest, doctorHeaders, HospitalErrorCode.NONWORKING_TIME);
        commissionRequest.setDate(LocalDate.of(2022, 1 ,3));
        commissionRequest.setTime(LocalTime.of(20,0));
        assertCreateHospitalException(commissionRequest, doctorHeaders, HospitalErrorCode.NONWORKING_TIME);
    }

    @Test
    public void testDeleteCommission() {
        WeekScheduleRequest scheduleRequest = new WeekScheduleRequest(LocalTime.of(8, 0), LocalTime.of(10, 0), Arrays.asList("Mon", "Wed"));
        CreateDoctorRequest doctorRequest = new CreateDoctorRequest(
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 10),
                scheduleRequest,
                null,
                15,
                "Андрей",
                "Попов",
                "Александрович",
                "Oculist",
                "15a",
                "popov1234567",
                "popov1234567");
        regDoctor(doctorRequest).getBody();
        CreatePatientRequest patientRequest = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov12345678", "popov12345678", "popov@mail.ru", "Gagarina, 14", "88005553535");
        ResponseEntity<PatientResponse> patientResponse = regPatient(patientRequest);
        HttpHeaders patientHeaders = login(new LoginRequest(patientRequest.getLogin(), patientRequest.getPassword()));
        HttpHeaders doctorHeaders = login(new LoginRequest(doctorRequest.getLogin(), doctorRequest.getPassword()));
        CommissionRequest commissionRequest = new CommissionRequest(patientResponse.getBody().getId(), new ArrayList<>(), doctorRequest.getRoom(), LocalDate.of(2022, 1, 3), LocalTime.of(8, 0), 40);
        CommissionResponse commissionResponse = createCommission(commissionRequest, doctorHeaders).getBody();
        TicketListResponse tickets = getTickets(patientHeaders).getBody();
        assertEquals(1, tickets.getTickets().size());
        deleteCommission(doctorHeaders, commissionResponse.getTicket());
        try {
            getTickets(patientHeaders);
            fail();
        } catch (HttpClientErrorException e) {
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getErrorCode(), HospitalErrorCode.PATIENT_HAS_NO_TICKET.toString());
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getField(), HospitalErrorCode.PATIENT_HAS_NO_TICKET.getField());
        }
    }

    @Test
    public void testDeleteCommissionWrongPatient() {
        WeekScheduleRequest scheduleRequest = new WeekScheduleRequest(LocalTime.of(8, 0), LocalTime.of(10, 0), Arrays.asList("Mon", "Wed"));
        CreateDoctorRequest doctorRequest = new CreateDoctorRequest(
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 10),
                scheduleRequest,
                null,
                15,
                "Андрей",
                "Попов",
                "Александрович",
                "Oculist",
                "15a",
                "popov1234567",
                "popov1234567");
        regDoctor(doctorRequest).getBody();
        HttpHeaders doctorHeaders = login(new LoginRequest(doctorRequest.getLogin(), doctorRequest.getPassword()));
        assertCreateHospitalException(doctorHeaders, "666666", HospitalErrorCode.TICKET_NOT_EXISTS);
    }

    @Test
    public void testDeleteStrangeCommission() {
        WeekScheduleRequest scheduleRequest = new WeekScheduleRequest(LocalTime.of(8, 0), LocalTime.of(10, 0), Arrays.asList("Mon", "Wed"));
        CreateDoctorRequest doctorRequest = new CreateDoctorRequest(
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 10),
                scheduleRequest,
                null,
                15,
                "Андрей",
                "Попов",
                "Александрович",
                "Oculist",
                "15a",
                "popov1234567",
                "popov1234567");
        regDoctor(doctorRequest).getBody();
        CreatePatientRequest patientRequest = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov12345678", "popov12345678", "popov@mail.ru", "Gagarina, 14", "88005553535");
        ResponseEntity<PatientResponse> patientResponse = regPatient(patientRequest);
        HttpHeaders doctorHeaders = login(new LoginRequest(doctorRequest.getLogin(), doctorRequest.getPassword()));
        CommissionRequest commissionRequest = new CommissionRequest(patientResponse.getBody().getId(), new ArrayList<>(), doctorRequest.getRoom(), LocalDate.of(2022, 1, 3), LocalTime.of(8, 0), 40);
        CommissionResponse commissionResponse = createCommission(commissionRequest, doctorHeaders).getBody();
        CreatePatientRequest patientRequest2 = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov123456789", "popov123456789", "popov@mail.ru", "Gagarina, 14", "88005553535");
        regPatient(patientRequest2);
        HttpHeaders patientHeaders = login(new LoginRequest(patientRequest2.getLogin(), patientRequest2.getPassword()));
        assertCreateHospitalException(patientHeaders, commissionResponse.getTicket(), HospitalErrorCode.NOT_ENOUGH_RIGHTS);
    }

    private void assertCreateHospitalException(CommissionRequest request, HttpHeaders headers, HospitalErrorCode code) {
        try {
            createCommission(request, headers);
            fail();
        } catch (HttpClientErrorException e) {
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getErrorCode(), code.toString());
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getField(), code.getField());
        }
    }

    private void assertCreateValidationException(CommissionRequest request, HttpHeaders headers, String code, String field) {
        try {
            createCommission(request, headers);
            fail();
        } catch (HttpClientErrorException e) {
            Type ErrorResponseList = new TypeToken<ArrayList<ErrorResponse>>() {
            }.getType();
            List<ErrorResponse> errors = gson.fromJson(e.getResponseBodyAsString(), ErrorResponseList);
            List<String> errorCodes = errors.stream().map(ErrorResponse::getErrorCode).collect(Collectors.toList());
            assertTrue(errorCodes.contains(code));
            assertEquals(errors.get(0).getField(), field);
        }
    }

    private void assertCreateHospitalException(HttpHeaders headers, String ticketNumber, HospitalErrorCode code) {
        try {
            deleteCommission(headers, ticketNumber);
            fail();
        } catch (HttpClientErrorException e) {
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getErrorCode(), code.toString());
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getField(), code.getField());
        }
    }

}
