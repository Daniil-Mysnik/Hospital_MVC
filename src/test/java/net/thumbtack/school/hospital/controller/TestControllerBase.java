package net.thumbtack.school.hospital.controller;

import com.google.gson.Gson;
import net.thumbtack.school.hospital.dto.request.*;
import net.thumbtack.school.hospital.dto.response.*;
import org.junit.Before;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;

public class TestControllerBase {
    protected RestTemplate template = new RestTemplate();
    protected int port = 8888;
    protected String host = "http://localhost:";
    protected Gson gson = new Gson();

    @Before
    public void clearDB() {
        template.exchange(host + port + "/api/debug/clear", HttpMethod.POST, new HttpEntity<>(null, null), EmptyResponse.class);
    }

    protected HttpHeaders loginMainAdmin() {
        LoginRequest request = new LoginRequest("admin12345", "admin12345");
        ResponseEntity<LoginResponse> response = template.exchange(host + port + "/api/sessions", HttpMethod.POST, new HttpEntity<>(request), LoginResponse.class);
        String cookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);
        return headers;
    }

    protected HttpHeaders login(LoginRequest request) {
        ResponseEntity<LoginResponse> response = template.exchange(host + port + "/api/sessions", HttpMethod.POST, new HttpEntity<>(request), LoginResponse.class);
        String cookie = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", cookie);
        return headers;
    }

    protected ResponseEntity<EmptyResponse> logout(HttpHeaders headers) {
        return template.exchange(host + port + "/api/sessions", HttpMethod.DELETE, new HttpEntity<>(headers), EmptyResponse.class);
    }

    protected ResponseEntity<AdminResponse> regAdmin(CreateAdminRequest request) {
        return template.exchange(host + port + "/api/admins", HttpMethod.POST, new HttpEntity<>(request, loginMainAdmin()), AdminResponse.class);
    }

    protected ResponseEntity<AdminResponse> updAdmin(UpdateAdminRequest request, HttpHeaders headers) {
        return template.exchange(host + port + "/api/admins", HttpMethod.PUT, new HttpEntity<>(request, headers), AdminResponse.class);
    }

    protected ResponseEntity<PatientResponse> regPatient(CreatePatientRequest request) {
        return template.exchange(host + port + "/api/patients", HttpMethod.POST, new HttpEntity<>(request, null), PatientResponse.class);
    }

    protected ResponseEntity<PatientResponse> updPatient(UpdatePatientRequest request, HttpHeaders headers) {
        return template.exchange(host + port + "/api/patients", HttpMethod.PUT, new HttpEntity<>(request, headers), PatientResponse.class);
    }

    protected ResponseEntity<DoctorWithScheduleResponse> regDoctor(CreateDoctorRequest request) {
        return template.exchange(host + port + "/api/doctors", HttpMethod.POST, new HttpEntity<>(request, loginMainAdmin()), DoctorWithScheduleResponse.class);
    }

    protected ResponseEntity<DoctorWithScheduleResponse> getDoctor(int id, String schedule, LocalDate dateStart, LocalDate dateEnd, HttpHeaders headers) {
        String url = host + port + "/api/doctors/" + id;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        if (schedule != null) {
            builder.queryParam("schedule", schedule);
        }
        if (dateStart != null) {
            builder.queryParam("dateStart", dateStart);
        }
        if (dateEnd != null) {
            builder.queryParam("dateEnd", dateEnd);
        }
        return template.exchange(builder.build().toUriString(), HttpMethod.GET, new HttpEntity<>(headers), DoctorWithScheduleResponse.class);
    }

    protected ResponseEntity<List<DoctorResponse>> getDoctorBySpeciality(String speciality, String schedule, LocalDate dateStart, LocalDate dateEnd, HttpHeaders headers) {
        String url = host + port + "/api/doctors";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        if (speciality != null) {
            builder.queryParam("speciality", speciality);
        }
        if (schedule != null) {
            builder.queryParam("schedule", schedule);
        }
        if (dateStart != null) {
            builder.queryParam("dateStart", dateStart);
        }
        if (dateEnd != null) {
            builder.queryParam("dateEnd", dateEnd);
        }
        return template.exchange(builder.build().toUriString(), HttpMethod.GET, new HttpEntity<>(headers), new ParameterizedTypeReference<List<DoctorResponse>>() {
        });
    }

    protected ResponseEntity<DoctorWithScheduleResponse> updSchedule(int id, UpdateScheduleRequest request) {
        return template.exchange(host + port + "/api/doctors/" + id, HttpMethod.PUT, new HttpEntity<>(request, loginMainAdmin()), DoctorWithScheduleResponse.class);
    }

    protected ResponseEntity<EmptyResponse> deleteDoctor(int id, DeleteDoctorRequest request) {
        return template.exchange(host + port + "/api/doctors/" + id, HttpMethod.DELETE, new HttpEntity<>(request, loginMainAdmin()), EmptyResponse.class);
    }

    protected ResponseEntity<TicketResponse> createTicket (TicketRequest request, HttpHeaders headers) {
        return template.exchange(host + port + "/api/tickets", HttpMethod.POST, new HttpEntity<>(request, headers), TicketResponse.class);
    }

    protected ResponseEntity<TicketListResponse> getTickets(HttpHeaders headers) {
        return template.exchange(host + port + "/api/tickets", HttpMethod.GET, new HttpEntity<>(null, headers), TicketListResponse.class);
    }

    protected ResponseEntity<EmptyResponse> deleteTicket(HttpHeaders headers, String ticketNumber) {
        return template.exchange(host + port + "/api/tickets/" + ticketNumber, HttpMethod.DELETE, new HttpEntity<>(headers), EmptyResponse.class);
    }

    protected ResponseEntity<CommissionResponse> createCommission(CommissionRequest request, HttpHeaders headers) {
        return template.exchange(host + port + "/api/commissions", HttpMethod.POST, new HttpEntity<>(request, headers), CommissionResponse.class);
    }

    protected ResponseEntity<EmptyResponse> deleteCommission(HttpHeaders headers, String ticketNumber) {
        return template.exchange(host + port + "/api/commissions/" + ticketNumber, HttpMethod.DELETE, new HttpEntity<>(headers), EmptyResponse.class);
    }

    protected ResponseEntity<SettingsResponse> getSettings(HttpHeaders headers) {
        return template.exchange(host + port + "/api/settings", HttpMethod.GET, new HttpEntity<>(null, headers), SettingsResponse.class);
    }

    protected ResponseEntity<DoctorWithScheduleResponse> getDoctorWithFreeTickets(int id, LocalDate dateStart, LocalDate dateEnd) {
        String url = host + port + "/api/statistic/doctor/" + id;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        builder.queryParam("schedule", "yes");
        if (dateStart != null) {
            builder.queryParam("dateStart", dateStart);
        }
        if (dateEnd != null) {
            builder.queryParam("dateEnd", dateEnd);
        }
        return template.exchange(builder.build().toUriString(), HttpMethod.GET, new HttpEntity<>(null), DoctorWithScheduleResponse.class);
    }

    protected ResponseEntity<CountOfFreeAppointmentsResponse> getDoctorWithFreeTicketsCount(int id, LocalDate dateStart, LocalDate dateEnd) {
        String url = host + port + "/api/statistic/doctor/" + id;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        if (dateStart != null) {
            builder.queryParam("dateStart", dateStart);
        }
        if (dateEnd != null) {
            builder.queryParam("dateEnd", dateEnd);
        }
        return template.exchange(builder.build().toUriString(), HttpMethod.GET, new HttpEntity<>(null), CountOfFreeAppointmentsResponse.class);
    }

    protected ResponseEntity<List<DoctorWithScheduleResponse>> getAllDoctorsWithFreeTickets(LocalDate dateStart, LocalDate dateEnd) {
        String url = host + port + "/api/statistic/doctors";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        builder.queryParam("schedule", "yes");
        if (dateStart != null) {
            builder.queryParam("dateStart", dateStart);
        }
        if (dateEnd != null) {
            builder.queryParam("dateEnd", dateEnd);
        }
        return template.exchange(builder.build().toUriString(), HttpMethod.GET, new HttpEntity<>(null), new ParameterizedTypeReference<List<DoctorWithScheduleResponse>>() {
        });
    }

    protected ResponseEntity<List<CountOfFreeAppointmentsResponse>> getDoctorWithFreeTicketsCount(LocalDate dateStart, LocalDate dateEnd) {
        String url = host + port + "/api/statistic/doctors";
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        if (dateStart != null) {
            builder.queryParam("dateStart", dateStart);
        }
        if (dateEnd != null) {
            builder.queryParam("dateEnd", dateEnd);
        }
        return template.exchange(builder.build().toUriString(), HttpMethod.GET, new HttpEntity<>(null), new ParameterizedTypeReference<List<CountOfFreeAppointmentsResponse>>() {
        });
    }

    protected ResponseEntity<BusyTicketsResponse> getPatientTickets(int id, HttpHeaders headers, LocalDate dateStart, LocalDate dateEnd) {
        String url = host + port + "/api/statistic/patient/" + id;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(url);
        builder.queryParam("ticket", "yes");
        if (dateStart != null) {
            builder.queryParam("dateStart", dateStart);
        }
        if (dateEnd != null) {
            builder.queryParam("dateEnd", dateEnd);
        }
        return template.exchange(builder.build().toUriString(), HttpMethod.GET, new HttpEntity<>(headers), BusyTicketsResponse.class);
    }

}
