package net.thumbtack.school.hospital.controller;

import com.google.gson.reflect.TypeToken;
import net.thumbtack.school.hospital.dto.request.*;
import net.thumbtack.school.hospital.dto.response.*;
import net.thumbtack.school.hospital.exceptions.HospitalErrorCode;
import net.thumbtack.school.hospital.model.Appointment;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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
public class TestDoctorController extends TestControllerBase {

    @Test
    public void testCreateDoctorWithFirstTypeSchedule() {
        WeekScheduleRequest scheduleRequest = new WeekScheduleRequest(LocalTime.of(8, 0), LocalTime.of(10, 0), Arrays.asList("Mon", "Wed"));
        CreateDoctorRequest request = new CreateDoctorRequest(
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
        DoctorWithScheduleResponse response = regDoctor(request).getBody();
        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getPatronymic(), response.getPatronymic());
        assertEquals(request.getSpeciality(), response.getSpeciality());
        assertEquals(request.getRoom(), response.getRoom());
        assertEquals(3, response.getSchedule().size());
        assertTrue(response.getSchedule().stream()
                .flatMap(scheduleResponse -> scheduleRequest.getWeekDays().stream())
                .collect(Collectors.toList()).contains("Mon"));
        assertTrue(response.getSchedule().stream()
                .flatMap(scheduleResponse -> scheduleRequest.getWeekDays().stream())
                .collect(Collectors.toList()).contains("Wed"));
        assertFalse(response.getSchedule().stream()
                .flatMap(scheduleResponse -> scheduleRequest.getWeekDays().stream())
                .collect(Collectors.toList()).contains("Fri"));
        assertFalse(response.getSchedule().stream()
                .flatMap(scheduleResponse -> scheduleRequest.getWeekDays().stream())
                .collect(Collectors.toList()).contains("Thu"));
        response.getSchedule().stream()
                .map(scheduleResponse -> scheduleRequest.getTimeStart())
                .forEach(time -> assertEquals(time, LocalTime.of(8, 0)));
        response.getSchedule().stream()
                .map(scheduleResponse -> scheduleRequest.getTimeEnd())
                .forEach(time -> assertEquals(time, LocalTime.of(10, 0)));
        assertTrue(response.getSchedule().get(0).getDate().isAfter(request.getDateStart().minusDays(1)));
        assertTrue(response.getSchedule().get(response.getSchedule().size() - 1).getDate().isBefore(request.getDateEnd().plusDays(1)));
    }

    @Test
    public void testCreateDoctorWithSecondTypeSchedule() {
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
                "15a",
                "popov1234567",
                "popov1234567");
        DoctorWithScheduleResponse response = regDoctor(request).getBody();
        assertEquals(request.getFirstName(), response.getFirstName());
        assertEquals(request.getLastName(), response.getLastName());
        assertEquals(request.getPatronymic(), response.getPatronymic());
        assertEquals(request.getSpeciality(), response.getSpeciality());
        assertEquals(request.getRoom(), response.getRoom());
        assertEquals(3, response.getSchedule().size());
        assertTrue(response.getSchedule().stream()
                .flatMap(scheduleResponse -> scheduleRequest.getDaySchedule().stream().map(AppointmentRequest::getWeekDay))
                .collect(Collectors.toList()).contains("Mon"));
        assertTrue(response.getSchedule().stream()
                .flatMap(scheduleResponse -> scheduleRequest.getDaySchedule().stream().map(AppointmentRequest::getWeekDay))
                .collect(Collectors.toList()).contains("Wed"));
        assertFalse(response.getSchedule().stream()
                .flatMap(scheduleResponse -> scheduleRequest.getDaySchedule().stream().map(AppointmentRequest::getWeekDay))
                .collect(Collectors.toList()).contains("Thu"));
        response.getSchedule().stream()
                .flatMap(scheduleResponse -> scheduleRequest.getDaySchedule().stream().
                        filter(appointmentRequest -> appointmentRequest.getWeekDay().equals("Mon")).
                        map(AppointmentRequest::getTimeStart))
                .forEach(time -> assertEquals(time, LocalTime.of(8, 0)));
        assertTrue(response.getSchedule().get(0).getDate().isAfter(request.getDateStart().minusDays(1)));
        assertTrue(response.getSchedule().get(response.getSchedule().size() - 1).getDate().isBefore(request.getDateEnd().plusDays(1)));
    }

    @Test
    public void testCreateDoctorWrongFirstName() {
        WeekDaysScheduleRequest scheduleRequest = new WeekDaysScheduleRequest(Arrays.asList(
                new AppointmentRequest("Mon", LocalTime.of(8, 0), LocalTime.of(10, 0)),
                new AppointmentRequest("Wed", LocalTime.of(9, 0), LocalTime.of(12, 0))));
        CreateDoctorRequest request = new CreateDoctorRequest(
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 10),
                null,
                scheduleRequest,
                15,
                "@",
                "Попов",
                "Александрович",
                "Oculist",
                "15a",
                "popov1234567",
                "popov1234567");
        assertValidationException(request, "PATTERN", "firstName");
        request.setFirstName(null);
        assertValidationException(request, "NOTNULL", "firstName");
        request.setFirstName("");
        assertValidationException(request, "NOTBLANK", "firstName");
        request.setFirstName("  ");
        assertValidationException(request, "NOTBLANK", "firstName");
        request.setFirstName("      ");
        assertValidationException(request, "NOTBLANK", "firstName");
        request.setFirstName("jhlasfdgbk");
        assertValidationException(request, "PATTERN", "firstName");
        request.setFirstName("jhlasfdasldmfhgysvnjfaskljhgfknadlcfhasdkcascfkasdufydasbknasdfbluasfasgasdfgbk");
        assertValidationException(request, "MAXLENGTH", "firstName");
        request.setFirstName("Ан-дрей");
        assertEquals(HttpStatus.OK, regDoctor(request).getStatusCode());
    }

    @Test
    public void testCreateDoctorWrongLastName() {
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
                "@",
                "Александрович",
                "Oculist",
                "15a",
                "popov1234567",
                "popov1234567");
        assertValidationException(request, "PATTERN", "lastName");
        request.setLastName(null);
        assertValidationException(request, "NOTNULL", "lastName");
        request.setLastName("");
        assertValidationException(request, "NOTBLANK", "lastName");
        request.setLastName("  ");
        assertValidationException(request, "NOTBLANK", "lastName");
        request.setLastName("      ");
        assertValidationException(request, "NOTBLANK", "lastName");
        request.setLastName("jhlasfdgbk");
        assertValidationException(request, "PATTERN", "lastName");
        request.setLastName("jhlasfdasldmfhgysvnjfaskljhgfknadlcfhasdkcascfkasdufydasbknasdfbluasfasgasdfgbk");
        assertValidationException(request, "MAXLENGTH", "lastName");
        request.setLastName("По-пов");
        assertEquals(HttpStatus.OK, regDoctor(request).getStatusCode());
    }

    @Test
    public void testCreateDoctorWrongPatronymic() {
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
                "@",
                "Oculist",
                "15a",
                "popov1234567",
                "popov1234567");
        assertValidationException(request, "BLANKORPATTERN", "patronymic");
        request.setPatronymic(null);
        assertEquals(HttpStatus.OK, regDoctor(request).getStatusCode());
        request.setPatronymic("");
        clearDB();
        assertEquals(HttpStatus.OK, regDoctor(request).getStatusCode());
        request.setPatronymic("  ");
        clearDB();
        assertEquals(HttpStatus.OK, regDoctor(request).getStatusCode());
        request.setPatronymic("      ");
        clearDB();
        assertEquals(HttpStatus.OK, regDoctor(request).getStatusCode());
        clearDB();
        request.setPatronymic("jhlasfdgbk");
        assertValidationException(request, "BLANKORPATTERN", "patronymic");
        request.setPatronymic("jhlasfdasldmfhgysvnjfaskljhgfknadlcfhasdkcascfkasdufydasbknasdfbluasfasgasdfgbk");
        assertValidationException(request, "MAXLENGTH", "patronymic");
        request.setPatronymic("Алек-сандрович");
        assertEquals(HttpStatus.OK, regDoctor(request).getStatusCode());
    }

    @Test
    public void testCreateDoctorWrongLogin() {
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
                "15a",
                "@",
                "popov1234567");
        assertValidationException(request, "PATTERN", "login");
        request.setLogin(null);
        assertValidationException(request, "NOTNULL", "login");
        request.setLogin("");
        assertValidationException(request, "NOTBLANK", "login");
        request.setLogin("  ");
        assertValidationException(request, "NOTBLANK", "login");
        request.setLogin("      ");
        assertValidationException(request, "NOTBLANK", "login");
        request.setLogin("jhlasfdasldmfhgysvnjfaskljhgfknadlcfhasdkcascfkasdufydasbknasdfbluasfasgasdfgbk");
        assertValidationException(request, "MAXLENGTH", "login");
    }

    @Test
    public void testCreateDoctorWrongPassword() {
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
                "15a",
                "popov1234567",
                "@");
        assertValidationException(request, "PASSWORDLENGTH", "password");
        request.setPassword(null);
        assertValidationException(request, "NOTNULL", "password");
        request.setPassword("");
        assertValidationException(request, "NOTBLANK", "password");
        request.setPassword("  ");
        assertValidationException(request, "NOTBLANK", "password");
        request.setPassword("      ");
        assertValidationException(request, "NOTBLANK", "password");
        request.setPassword("jhlasfdasldmfhgysvnjfaskljhgfknadlcfhasdkcascfkasdufydasbknasdfbluasfasgasdfgbk");
        assertValidationException(request, "MAXLENGTH", "password");
    }

    @Test
    public void testCreateDoctorWrongSpeciality() {
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
                "asidflg",
                "15a",
                "popov1234567",
                "popov1234567");
        assertHospitalException(request, HospitalErrorCode.INCORRECT_SPECIALITY);
        request.setSpeciality(null);
        assertValidationException(request, "NOTNULL", "speciality");
        request.setSpeciality("");
        assertValidationException(request, "NOTBLANK", "speciality");
        request.setSpeciality("  ");
        assertValidationException(request, "NOTBLANK", "speciality");
        request.setSpeciality("      ");
        assertValidationException(request, "NOTBLANK", "speciality");
    }

    @Test
    public void testCreateDoctorWrongRoom() {
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
                "666",
                "popov1234567",
                "popov1234567");
        assertHospitalException(request, HospitalErrorCode.WRONG_ROOM);
        request.setRoom(null);
        assertValidationException(request, "NOTNULL", "room");
        request.setRoom("");
        assertValidationException(request, "NOTBLANK", "room");
        request.setRoom("  ");
        assertValidationException(request, "NOTBLANK", "room");
        request.setRoom("      ");
        assertValidationException(request, "NOTBLANK", "room");
    }

    @Test
    public void testCreateDoctorWithTwoTypesSchedule() {
        WeekScheduleRequest scheduleRequest = new WeekScheduleRequest(LocalTime.of(8, 0), LocalTime.of(10, 0), Arrays.asList("Mon", "Wed"));
        WeekDaysScheduleRequest scheduleRequest2 = new WeekDaysScheduleRequest(Arrays.asList(
                new AppointmentRequest("Mon", LocalTime.of(8, 0), LocalTime.of(10, 0)),
                new AppointmentRequest("Wed", LocalTime.of(9, 0), LocalTime.of(12, 0))));
        CreateDoctorRequest request = new CreateDoctorRequest(
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 10),
                scheduleRequest,
                scheduleRequest2,
                15,
                "Андрей",
                "Попов",
                "Александрович",
                "Oculist",
                "666",
                "popov1234567",
                "popov1234567");
        assertValidationException(request, "ONESCHEDULE", "schedule");
    }

    @Test
    public void testCreateDoctorWithWrongDate() {
        WeekDaysScheduleRequest scheduleRequest = new WeekDaysScheduleRequest(Arrays.asList(
                new AppointmentRequest("Mon", LocalTime.of(8, 0), LocalTime.of(10, 0)),
                new AppointmentRequest("Wed", LocalTime.of(9, 0), LocalTime.of(12, 0))));
        CreateDoctorRequest request = new CreateDoctorRequest(
                LocalDate.of(1950, 1, 1),
                LocalDate.of(1950, 1, 10),
                null,
                scheduleRequest,
                15,
                "Андрей",
                "Попов",
                "Александрович",
                "Oculist",
                "15a",
                "popov1234567",
                "popov1234567");
        assertValidationException(request, "DATE", "date");
        request.setDateStart(null);
        assertValidationException(request, "DATE", "date");
    }

    @Test
    public void testCreateDoctorWithLongWorkShift() {
        WeekDaysScheduleRequest scheduleRequest = new WeekDaysScheduleRequest(Arrays.asList(
                new AppointmentRequest("Mon", LocalTime.of(8, 0), LocalTime.of(23, 0)),
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
        assertValidationException(request, "TIME", "time");
        scheduleRequest.getDaySchedule().get(0).setTimeStart(null);
        assertValidationException(request, "TIME", "time");
    }

    @Test
    public void testCreateDoctorWithWrongWeekDay() {
        WeekDaysScheduleRequest scheduleRequest = new WeekDaysScheduleRequest(Arrays.asList(
                new AppointmentRequest("KDgyfsg", LocalTime.of(8, 0), LocalTime.of(10, 0)),
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
        assertValidationException(request, "WEEKDAY", "weekDay");
        scheduleRequest.getDaySchedule().get(0).setWeekDay("Sun");
        assertValidationException(request, "WEEKDAY", "weekDay");
    }

    @Test
    public void testCreateDoctorWithWrongDuration() {
        WeekDaysScheduleRequest scheduleRequest = new WeekDaysScheduleRequest(Arrays.asList(
                new AppointmentRequest("Mon", LocalTime.of(8, 0), LocalTime.of(10, 0)),
                new AppointmentRequest("Wed", LocalTime.of(9, 0), LocalTime.of(12, 0))));
        CreateDoctorRequest request = new CreateDoctorRequest(
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2022, 1, 10),
                null,
                scheduleRequest,
                1,
                "Андрей",
                "Попов",
                "Александрович",
                "Oculist",
                "1",
                "popov1234567",
                "popov1234567");
        assertValidationException(request, "MIN", "duration");
        request.setDuration(60);
        assertValidationException(request, "MAX", "duration");
    }

    @Test
    public void testGetDoctorFull() {
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
        HttpHeaders headers = loginMainAdmin();
        DoctorWithScheduleResponse getDoctorResponse = getDoctor(response.getBody().getId(), "yes", LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 10), headers).getBody();
        assertEquals(request.getFirstName(), getDoctorResponse.getFirstName());
        assertEquals(request.getLastName(), getDoctorResponse.getLastName());
        assertEquals(request.getPatronymic(), getDoctorResponse.getPatronymic());
        assertEquals(request.getSpeciality(), getDoctorResponse.getSpeciality());
        assertEquals(request.getRoom(), getDoctorResponse.getRoom());
        assertEquals(3, getDoctorResponse.getSchedule().size());
        assertTrue(getDoctorResponse.getSchedule().stream()
                .flatMap(scheduleResponse -> scheduleRequest.getDaySchedule().stream().map(AppointmentRequest::getWeekDay))
                .collect(Collectors.toList()).contains("Mon"));
        assertTrue(getDoctorResponse.getSchedule().stream()
                .flatMap(scheduleResponse -> scheduleRequest.getDaySchedule().stream().map(AppointmentRequest::getWeekDay))
                .collect(Collectors.toList()).contains("Wed"));
        assertFalse(getDoctorResponse.getSchedule().stream()
                .flatMap(scheduleResponse -> scheduleRequest.getDaySchedule().stream().map(AppointmentRequest::getWeekDay))
                .collect(Collectors.toList()).contains("Thu"));
        getDoctorResponse.getSchedule().stream()
                .flatMap(scheduleResponse -> scheduleRequest.getDaySchedule().stream().
                        filter(appointmentRequest -> appointmentRequest.getWeekDay().equals("Mon")).
                        map(AppointmentRequest::getTimeStart))
                .forEach(time -> assertEquals(time, LocalTime.of(8, 0)));
        assertTrue(getDoctorResponse.getSchedule().get(0).getDate().isAfter(request.getDateStart().minusDays(1)));
        assertTrue(getDoctorResponse.getSchedule().get(getDoctorResponse.getSchedule().size() - 1).getDate().isBefore(request.getDateEnd().plusDays(1)));
    }

    @Test
    public void testDetDoctorWithoutSchedule() {
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
        HttpHeaders headers = loginMainAdmin();
        DoctorWithScheduleResponse getDoctorResponse = getDoctor(response.getBody().getId(), null, LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 10), headers).getBody();
        assertEquals(request.getFirstName(), getDoctorResponse.getFirstName());
        assertEquals(request.getLastName(), getDoctorResponse.getLastName());
        assertEquals(request.getPatronymic(), getDoctorResponse.getPatronymic());
        assertEquals(request.getSpeciality(), getDoctorResponse.getSpeciality());
        assertEquals(request.getRoom(), getDoctorResponse.getRoom());
        assertNull(getDoctorResponse.getSchedule());
    }

    @Test
    public void testGetDoctorByTerm() {
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
        HttpHeaders headers = loginMainAdmin();
        DoctorWithScheduleResponse getDoctorResponse = getDoctor(response.getBody().getId(), "yes", LocalDate.of(2022, 1, 5), null, headers).getBody();
        assertEquals(request.getFirstName(), getDoctorResponse.getFirstName());
        assertEquals(request.getLastName(), getDoctorResponse.getLastName());
        assertEquals(request.getPatronymic(), getDoctorResponse.getPatronymic());
        assertEquals(request.getSpeciality(), getDoctorResponse.getSpeciality());
        assertEquals(request.getRoom(), getDoctorResponse.getRoom());
        assertEquals(2, getDoctorResponse.getSchedule().size());
    }

    @Test
    public void testGetDoctorBySpeciality() {
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
                "фжлаыпвиры",
                "ывапывп",
                "ывапывпа",
                "Therapist",
                "15a",
                "popov12345678",
                "popov1234567");
        List<DoctorResponse> responses = getDoctorBySpeciality("Oculist", "yes", LocalDate.of(2022, 1, 1), LocalDate.of(2022, 1, 10), loginMainAdmin()).getBody();
        assertEquals(1, responses.size());
        assertEquals(request1.getFirstName(), responses.get(0).getFirstName());
        assertEquals(request1.getLastName(), responses.get(0).getLastName());
        assertEquals(request1.getPatronymic(), responses.get(0).getPatronymic());
        assertEquals(request1.getSpeciality(), responses.get(0).getSpeciality());
        assertEquals(request1.getRoom(), responses.get(0).getRoom());
    }

    @Test
    public void testUpdateDoctor() {
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
        UpdateScheduleRequest updateRequest = new UpdateScheduleRequest(LocalDate.of(2022, 1, 4),
                LocalDate.of(2022, 1, 20),
                null,
                scheduleRequest1,
                20);
        DoctorWithScheduleResponse updResponse = updSchedule(response1.getBody().getId(), updateRequest).getBody();
        assertEquals(request1.getFirstName(), updResponse.getFirstName());
        assertEquals(request1.getLastName(), updResponse.getLastName());
        assertEquals(request1.getPatronymic(), updResponse.getPatronymic());
        assertEquals(request1.getSpeciality(), updResponse.getSpeciality());
        assertEquals(request1.getRoom(), updResponse.getRoom());
        assertEquals(6, updResponse.getSchedule().size());
    }

    @Test
    public void testUpdateDoctorWhenAppointmentBusy() {
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
        CreatePatientRequest patientRequest = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov12345678", "popov12345678", "popov@mail.ru", "Gagarina, 14", "88005553535");
        ResponseEntity<PatientResponse> patientResponse = regPatient(patientRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", patientResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
        createTicket(new TicketRequest(response1.getBody().getId(), null, LocalDate.of(2022, 1, 10), LocalTime.of(8, 0)), headers);
        UpdateScheduleRequest updateRequest = new UpdateScheduleRequest(LocalDate.of(2022, 1, 4),
                LocalDate.of(2022, 1, 20),
                null,
                scheduleRequest1,
                20);
        assertUpdateHospitalException(response1.getBody().getId(), updateRequest, HospitalErrorCode.BUSY_TICKET);
    }

    @Test
    public void testDeleteDoctor() {
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
        DeleteDoctorRequest request = new DeleteDoctorRequest(LocalDate.of(2022, 1, 9));
        deleteDoctor(response1.getBody().getId(), request);
        DoctorWithScheduleResponse getDocResponse = getDoctor(response1.getBody().getId(), "yes", LocalDate.of(2022, 1, 1), null, loginMainAdmin()).getBody();
        assertEquals(0, getDocResponse.getSchedule().stream().filter(scheduleResponse -> scheduleResponse.getDate().isAfter(LocalDate.of(2022, 1, 9))).count());
    }

    @Test
    public void testDeleteDoctorWhenAppointmentBusy() {
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
        CreatePatientRequest patientRequest = new CreatePatientRequest("Андрей", "Попов", "Александрович", "popov12345678", "popov12345678", "popov@mail.ru", "Gagarina, 14", "88005553535");
        ResponseEntity<PatientResponse> patientResponse = regPatient(patientRequest);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", patientResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
        createTicket(new TicketRequest(response1.getBody().getId(), null, LocalDate.of(2022, 1, 10), LocalTime.of(8, 0)), headers);
        DeleteDoctorRequest request = new DeleteDoctorRequest(LocalDate.of(2022, 1, 5));
        assertEquals(HttpStatus.OK, deleteDoctor(response1.getBody().getId(), request).getStatusCode());
        HttpHeaders headers1 = login(new LoginRequest(patientRequest.getLogin(), patientRequest.getPassword()));
        try {
            getTickets(headers1);
            fail();
        } catch (HttpClientErrorException e) {
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getErrorCode(), HospitalErrorCode.PATIENT_HAS_NO_TICKET.toString());
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getField(), HospitalErrorCode.PATIENT_HAS_NO_TICKET.getField());
        }
    }

    @Test
    public void testDeleteDoctorWrongDate() {
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
        DeleteDoctorRequest request = new DeleteDoctorRequest(LocalDate.of(1950, 1, 5));
        try {
            deleteDoctor(response1.getBody().getId(), request);
            fail();
        } catch (HttpClientErrorException e) {
            Type ErrorResponseList = new TypeToken<ArrayList<ErrorResponse>>() {
            }.getType();
            List<ErrorResponse> errors = gson.fromJson(e.getResponseBodyAsString(), ErrorResponseList);
            List<String> errorCodes = errors.stream().map(ErrorResponse::getErrorCode).collect(Collectors.toList());
            assertTrue(errorCodes.contains("FUTURE"));
            assertEquals(errors.get(0).getField(), "date");
        }
    }

    private void assertHospitalException(CreateDoctorRequest request, HospitalErrorCode code) {
        try {
            regDoctor(request);
            fail();
        } catch (HttpClientErrorException e) {
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getErrorCode(), code.toString());
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getField(), code.getField());
        }
    }

    private void assertValidationException(CreateDoctorRequest request, String code, String field) {
        try {
            regDoctor(request);
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

    private void assertUpdateHospitalException(int id, UpdateScheduleRequest request, HospitalErrorCode code) {
        try {
            updSchedule(id, request);
            fail();
        } catch (HttpClientErrorException e) {
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getErrorCode(), code.toString());
            assertEquals(gson.fromJson(e.getResponseBodyAsString(), ErrorResponse.class).getField(), code.getField());
        }
    }

}
