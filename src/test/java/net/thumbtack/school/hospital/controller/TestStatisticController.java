package net.thumbtack.school.hospital.controller;

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

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestStatisticController extends TestControllerBase {

    @Test
    public void testGetDoctorFreeTickets() {
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
        DoctorWithScheduleResponse doctorWithFreeTickets1 = getDoctorWithFreeTickets(response.getBody().getId(), LocalDate.of(2022, 1, 1), null).getBody();

        assertEquals(28, (int) doctorWithFreeTickets1.getSchedule().stream()
                .mapToLong(scheduleResponse -> scheduleResponse.getDaySchedule().size())
                .sum());
        assertEquals(doctorWithFreeTickets1.getFirstName(), request.getFirstName());
        assertEquals(doctorWithFreeTickets1.getLastName(), request.getLastName());
        assertEquals(doctorWithFreeTickets1.getPatronymic(), request.getPatronymic());
        assertEquals(doctorWithFreeTickets1.getRoom(), request.getRoom());
        assertEquals(doctorWithFreeTickets1.getSpeciality(), request.getSpeciality());

        CountOfFreeAppointmentsResponse doctorWithFreeTicketsCount1 = getDoctorWithFreeTicketsCount(response.getBody().getId(), LocalDate.of(2022, 1, 1), null).getBody();
        assertEquals(28, doctorWithFreeTicketsCount1.getCount());

        CreatePatientRequest patientRequest = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov12345678", "popov12345678", "popov@mail.ru", "Gagarina, 14", "88005553535");
        regPatient(patientRequest).getBody();
        HttpHeaders headers = login(new LoginRequest(patientRequest.getLogin(), patientRequest.getPassword()));

        TicketRequest ticketRequest = new TicketRequest(response.getBody().getId(), null, LocalDate.of(2022, 1, 3), LocalTime.of(8, 0));
        createTicket(ticketRequest, headers);

        DoctorWithScheduleResponse doctorWithFreeTickets2 = getDoctorWithFreeTickets(response.getBody().getId(), LocalDate.of(2022, 1, 1), null).getBody();
        assertEquals(27, (int) doctorWithFreeTickets2.getSchedule().stream()
                .mapToLong(scheduleResponse -> scheduleResponse.getDaySchedule().size())
                .sum());

        CountOfFreeAppointmentsResponse doctorWithFreeTicketsCount2 = getDoctorWithFreeTicketsCount(response.getBody().getId(), LocalDate.of(2022, 1, 1), null).getBody();
        assertEquals(27, doctorWithFreeTicketsCount2.getCount());
    }

    @Test
    public void testGetAllDoctorsFreeTickets() {
        WeekDaysScheduleRequest scheduleRequest1 = new WeekDaysScheduleRequest(Arrays.asList(
                new AppointmentRequest("Mon", LocalTime.of(8, 0), LocalTime.of(10, 0)),
                new AppointmentRequest("Wed", LocalTime.of(9, 0), LocalTime.of(12, 0))));
        CreateDoctorRequest request1 = new CreateDoctorRequest(
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 10),
                null,
                scheduleRequest1,
                15,
                "Андрей",
                "Попов",
                "Александрович",
                "Oculist",
                "1",
                "popov1234567",
                "popov1234567");
        ResponseEntity<DoctorWithScheduleResponse> response1 = regDoctor(request1);

        WeekDaysScheduleRequest scheduleRequest2 = new WeekDaysScheduleRequest(Arrays.asList(
                new AppointmentRequest("Mon", LocalTime.of(8, 0), LocalTime.of(10, 0)),
                new AppointmentRequest("Wed", LocalTime.of(9, 0), LocalTime.of(12, 0))));
        CreateDoctorRequest request2 = new CreateDoctorRequest(
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 10),
                null,
                scheduleRequest2,
                15,
                "Андрей",
                "Попов",
                "Александрович",
                "Oculist",
                "15a",
                "popov12345678",
                "popov12345678");
        ResponseEntity<DoctorWithScheduleResponse> response2 = regDoctor(request2);

        List<DoctorWithScheduleResponse> doctorsWithFreeTickets1 = getAllDoctorsWithFreeTickets(LocalDate.of(2022, 1, 1), null).getBody();
        assertEquals(2, doctorsWithFreeTickets1.size());
        assertEquals(56, doctorsWithFreeTickets1.stream()
                .flatMap(doctorWithScheduleResponse -> doctorWithScheduleResponse.getSchedule().stream()
                        .flatMap(scheduleResponse -> scheduleResponse.getDaySchedule().stream())).count());

        List<CountOfFreeAppointmentsResponse> doctorsWithFreeTicketsCount1 = getDoctorWithFreeTicketsCount(LocalDate.of(2022, 1, 1), null).getBody();
        assertEquals(56, (int) doctorsWithFreeTicketsCount1.stream()
                .map(CountOfFreeAppointmentsResponse::getCount)
                .reduce(0, Integer::sum));

        CreatePatientRequest patientRequest = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov123456789", "popov123456789", "popov@mail.ru", "Gagarina, 14", "88005553535");
        regPatient(patientRequest).getBody();
        HttpHeaders headers = login(new LoginRequest(patientRequest.getLogin(), patientRequest.getPassword()));

        TicketRequest ticketRequest = new TicketRequest(response1.getBody().getId(), null, LocalDate.of(2022, 1, 3), LocalTime.of(8, 0));
        createTicket(ticketRequest, headers);
        ticketRequest.setDoctorId(response2.getBody().getId());
        ticketRequest.setTime(LocalTime.of(9, 0));
        createTicket(ticketRequest, headers);

        List<DoctorWithScheduleResponse> doctorsWithFreeTickets2 = getAllDoctorsWithFreeTickets(LocalDate.of(2022, 1, 1), null).getBody();
        assertEquals(54, doctorsWithFreeTickets2.stream()
                .flatMap(doctorWithScheduleResponse -> doctorWithScheduleResponse.getSchedule().stream()
                        .flatMap(scheduleResponse -> scheduleResponse.getDaySchedule().stream())).count());

        List<CountOfFreeAppointmentsResponse> doctorsWithFreeTicketsCount2 = getDoctorWithFreeTicketsCount(LocalDate.of(2022, 1, 1), null).getBody();
        assertEquals(54, (int) doctorsWithFreeTicketsCount2.stream()
                .map(CountOfFreeAppointmentsResponse::getCount)
                .reduce(0, Integer::sum));
    }

    @Test
    public void testGetPatientTicketsByNotPatient() {
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

        CreatePatientRequest patientRequest = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov123456789", "popov123456789", "popov@mail.ru", "Gagarina, 14", "88005553535");
        PatientResponse patientResponse = regPatient(patientRequest).getBody();
        HttpHeaders headers = login(new LoginRequest(patientRequest.getLogin(), patientRequest.getPassword()));

        TicketRequest ticketRequest = new TicketRequest(response.getBody().getId(), null, LocalDate.of(2022, 1, 3), LocalTime.of(8, 0));
        createTicket(ticketRequest, headers);
        ticketRequest.setDate(LocalDate.of(2022, 1, 10));
        createTicket(ticketRequest, headers);

        BusyTicketsResponse patientWithTickets = getPatientTickets(patientResponse.getId(), loginMainAdmin(), LocalDate.of(2022, 1, 1), null).getBody();
        assertEquals(patientResponse.getId(), patientWithTickets.getId());
        assertEquals(patientResponse.getFirstName(), patientWithTickets.getFirstName());
        assertEquals(patientResponse.getLastName(), patientWithTickets.getLastName());
        assertEquals(patientResponse.getPatronymic(), patientWithTickets.getPatronymic());
        assertEquals(patientResponse.getEmail(), patientWithTickets.getEmail());
        assertEquals(patientResponse.getAddress(), patientWithTickets.getAddress());
        assertEquals(patientResponse.getPhone(), patientWithTickets.getPhone());
        assertEquals(2, patientWithTickets.getBusyTickets().size());
        assertEquals(LocalDate.of(2022, 1, 3), patientWithTickets.getBusyTickets().get(0).getDate());
        assertEquals(LocalTime.of(8, 0), patientWithTickets.getBusyTickets().get(0).getTime());
        assertEquals(response.getBody().getRoom(), patientWithTickets.getBusyTickets().get(0).getRoom());
        assertEquals(LocalDate.of(2022, 1, 10), patientWithTickets.getBusyTickets().get(1).getDate());
        assertEquals(LocalTime.of(8, 0), patientWithTickets.getBusyTickets().get(1).getTime());
        assertEquals(response.getBody().getRoom(), patientWithTickets.getBusyTickets().get(1).getRoom());
    }

    @Test
    public void testGetPatientByStrangePatient() {
        CreatePatientRequest patientRequest = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov123456789", "popov123456789", "popov@mail.ru", "Gagarina, 14", "88005553535");
        PatientResponse patientResponse = regPatient(patientRequest).getBody();
        HttpHeaders headers = login(new LoginRequest(patientRequest.getLogin(), patientRequest.getPassword()));
        try {
            getPatientTickets(patientResponse.getId(), headers, LocalDate.of(2022, 1, 1), null).getBody();
            fail();
        } catch (HttpClientErrorException e) {
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getErrorCode(), HospitalErrorCode.NOT_ENOUGH_RIGHTS.toString());
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getField(), HospitalErrorCode.NOT_ENOUGH_RIGHTS.getField());
        }
    }

}
