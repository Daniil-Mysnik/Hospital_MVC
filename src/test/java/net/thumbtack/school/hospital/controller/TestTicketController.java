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
public class TestTicketController extends TestControllerBase {

    @Test
    public void testCreateTicket() {
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
        CreatePatientRequest patientRequest = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov12345678", "popov12345678", "popov@mail.ru", "Gagarina, 14", "88005553535");
        regPatient(patientRequest);
        HttpHeaders headers = login(new LoginRequest(patientRequest.getLogin(), patientRequest.getPassword()));
        TicketRequest ticketRequest = new TicketRequest(response.getBody().getId(), null, LocalDate.of(2022, 1, 3), LocalTime.of(8, 0));
        TicketResponse ticketResponse = createTicket(ticketRequest, headers).getBody();
        assertEquals(ticketRequest.getDate(), ticketResponse.getDate());
        assertEquals(ticketRequest.getTime(), ticketResponse.getTime());
        assertEquals(response.getBody().getRoom(), ticketResponse.getRoom());
        assertNotNull(ticketResponse.getTicket());
    }

    @Test
    public void testCreateTicketWhenItBusy() {
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
        CreatePatientRequest patientRequest = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov12345678", "popov12345678", "popov@mail.ru", "Gagarina, 14", "88005553535");
        regPatient(patientRequest);
        HttpHeaders headers = login(new LoginRequest(patientRequest.getLogin(), patientRequest.getPassword()));
        TicketRequest ticketRequest = new TicketRequest(response.getBody().getId(), null, LocalDate.of(2022, 1, 3), LocalTime.of(8, 0));
        createTicket(ticketRequest, headers).getBody();
        assertException(ticketRequest, headers, HospitalErrorCode.BUSY_TICKET);
    }

    @Test
    public void testCreateTicketWithIdAndSpeciality() {
        CreatePatientRequest patientRequest = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov12345678", "popov12345678", "popov@mail.ru", "Gagarina, 14", "88005553535");
        regPatient(patientRequest);
        HttpHeaders headers = login(new LoginRequest(patientRequest.getLogin(), patientRequest.getPassword()));
        TicketRequest ticketRequest = new TicketRequest(666, "Ofasijf", LocalDate.of(2022, 1, 3), LocalTime.of(8, 0));
        assertValidationException(ticketRequest, headers, "IDORSPECIALITY", "doctor");
    }

    @Test
    public void testCreateTicketNonWorkingTime() {
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
        CreatePatientRequest patientRequest = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov12345678", "popov12345678", "popov@mail.ru", "Gagarina, 14", "88005553535");
        regPatient(patientRequest);
        HttpHeaders headers = login(new LoginRequest(patientRequest.getLogin(), patientRequest.getPassword()));
        TicketRequest ticketRequest = new TicketRequest(response.getBody().getId(), null, LocalDate.of(2022, 1, 3), LocalTime.of(18, 0));
        assertException(ticketRequest, headers, HospitalErrorCode.NONWORKING_TIME);
    }

    @Test
    public void testCreateTicketWithPastTense() {
        CreatePatientRequest patientRequest = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov12345678", "popov12345678", "popov@mail.ru", "Gagarina, 14", "88005553535");
        regPatient(patientRequest);
        HttpHeaders headers = login(new LoginRequest(patientRequest.getLogin(), patientRequest.getPassword()));
        TicketRequest ticketRequest = new TicketRequest(666, null, LocalDate.of(1950, 1, 3), LocalTime.of(8, 0));
        assertValidationException(ticketRequest, headers, "FUTURE", "date");
    }

    @Test
    public void testGetTicketList() {
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
        CreatePatientRequest patientRequest = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov12345678", "popov12345678", "popov@mail.ru", "Gagarina, 14", "88005553535");
        PatientResponse patientResponse = regPatient(patientRequest).getBody();
        HttpHeaders headers = login(new LoginRequest(patientRequest.getLogin(), patientRequest.getPassword()));
        TicketRequest ticketRequest = new TicketRequest(response.getBody().getId(), null, LocalDate.of(2022, 1, 3), LocalTime.of(8, 0));
        createTicket(ticketRequest, headers).getBody();
        ticketRequest.setTime(LocalTime.of(8, 15));
        createTicket(ticketRequest, headers).getBody();
        HttpHeaders doctorHeaders = login(new LoginRequest(request.getLogin(), request.getPassword()));
        createCommission(new CommissionRequest(patientResponse.getId(), new ArrayList<>(), "1", LocalDate.of(2022, 1, 3), LocalTime.of(8, 30), 30), doctorHeaders);
        TicketListResponse tickets = getTickets(headers).getBody();
        assertEquals(3, tickets.getTickets().size());
        assertEquals(LocalDate.of(2022, 1, 3), tickets.getTickets().get(0).getDate());
        assertEquals(LocalDate.of(2022, 1, 3), tickets.getTickets().get(1).getDate());
        assertEquals(LocalDate.of(2022, 1, 3), tickets.getTickets().get(2).getDate());
        assertEquals(LocalTime.of(8, 0), tickets.getTickets().get(0).getTime());
        assertEquals(LocalTime.of(8, 15), tickets.getTickets().get(1).getTime());
        assertEquals(LocalTime.of(8, 30), tickets.getTickets().get(2).getTime());
        assertEquals("1", tickets.getTickets().get(0).getRoom());
        assertEquals("1", tickets.getTickets().get(1).getRoom());
        assertEquals("1", tickets.getTickets().get(2).getRoom());
    }

    @Test
    public void testGetEmptyTicketList() {
        CreatePatientRequest patientRequest = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov12345678", "popov12345678", "popov@mail.ru", "Gagarina, 14", "88005553535");
        regPatient(patientRequest).getBody();
        HttpHeaders headers = login(new LoginRequest(patientRequest.getLogin(), patientRequest.getPassword()));
        try {
            getTickets(headers);
            fail();
        } catch (HttpClientErrorException e) {
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getErrorCode(), HospitalErrorCode.PATIENT_HAS_NO_TICKET.toString());
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getField(), HospitalErrorCode.PATIENT_HAS_NO_TICKET.getField());
        }
    }

    @Test
    public void testDeleteTicket() {
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
        CreatePatientRequest patientRequest = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov12345678", "popov12345678", "popov@mail.ru", "Gagarina, 14", "88005553535");
        regPatient(patientRequest).getBody();
        HttpHeaders headers = login(new LoginRequest(patientRequest.getLogin(), patientRequest.getPassword()));
        TicketRequest ticketRequest = new TicketRequest(response.getBody().getId(), null, LocalDate.of(2022, 1, 3), LocalTime.of(8, 0));
        TicketResponse ticketResponse = createTicket(ticketRequest, headers).getBody();
        ticketRequest.setTime(LocalTime.of(8, 15));
        createTicket(ticketRequest, headers);
        TicketListResponse tickets = getTickets(headers).getBody();
        assertEquals(2, tickets.getTickets().size());
        deleteTicket(headers, ticketResponse.getTicket());
        TicketListResponse tickets2 = getTickets(headers).getBody();
        assertEquals(1, tickets2.getTickets().size());
    }

    @Test
    public void testDeleteWrongTicket() {
        CreatePatientRequest patientRequest = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov12345678", "popov12345678", "popov@mail.ru", "Gagarina, 14", "88005553535");
        regPatient(patientRequest).getBody();
        HttpHeaders headers = login(new LoginRequest(patientRequest.getLogin(), patientRequest.getPassword()));
        try {
            deleteTicket(headers, "SomeWrongTicket");
            fail();
        } catch (HttpClientErrorException e) {
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getErrorCode(), HospitalErrorCode.TICKET_NOT_EXISTS.toString());
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getField(), HospitalErrorCode.TICKET_NOT_EXISTS.getField());
        }
    }

    @Test
    public void testDeleteStrangerTicket() {
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

        CreatePatientRequest patientRequest = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov12345678", "popov12345678", "popov@mail.ru", "Gagarina, 14", "88005553535");
        regPatient(patientRequest).getBody();

        HttpHeaders headers = login(new LoginRequest(patientRequest.getLogin(), patientRequest.getPassword()));
        TicketRequest ticketRequest = new TicketRequest(response.getBody().getId(), null, LocalDate.of(2022, 1, 3), LocalTime.of(8, 0));
        TicketResponse ticketResponse = createTicket(ticketRequest, headers).getBody();

        patientRequest.setLogin("popov123456789");
        regPatient(patientRequest).getBody();
        HttpHeaders headers2 = login(new LoginRequest(patientRequest.getLogin(), patientRequest.getPassword()));

        try {
            deleteTicket(headers2, ticketResponse.getTicket());
            fail();
        } catch (HttpClientErrorException e) {
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getErrorCode(), HospitalErrorCode.NOT_ENOUGH_RIGHTS.toString());
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getField(), HospitalErrorCode.NOT_ENOUGH_RIGHTS.getField());
        }
    }

    private void assertException(TicketRequest request, HttpHeaders headers, HospitalErrorCode code) {
        try {
            createTicket(request, headers);
            fail();
        } catch (HttpClientErrorException e) {
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getErrorCode(), code.toString());
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getField(), code.getField());
        }
    }

    private void assertValidationException(TicketRequest request, HttpHeaders headers, String code, String field) {
        try {
            createTicket(request, headers);
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
