package net.thumbtack.school.hospital.controller;

import net.thumbtack.school.hospital.dto.request.AppointmentRequest;
import net.thumbtack.school.hospital.dto.request.CreateDoctorRequest;
import net.thumbtack.school.hospital.dto.request.WeekDaysScheduleRequest;
import net.thumbtack.school.hospital.dto.request.WeekScheduleRequest;
import net.thumbtack.school.hospital.dto.response.DoctorResponse;
import net.thumbtack.school.hospital.dto.response.LoginResponse;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.UserType;
import net.thumbtack.school.hospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class DoctorController {
    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("registerDoctor")
    public String register(HttpServletRequest request) {
        LoginResponse loginResponse = (LoginResponse) request.getSession().getAttribute("login");
        if (loginResponse == null || !loginResponse.getUserResponse().getUserType().equals(UserType.ADMIN)) {
            return "403";
        }
        return "registerDoctor";
    }

    @PostMapping("registerDoctor")
    public String create(@RequestParam String firstName,
                         @RequestParam String lastName,
                         @RequestParam String patronymic,
                         @RequestParam String speciality,
                         @RequestParam String room,
                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateStart,
                         @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateEnd,
                         @RequestParam(value = "isWeekDaysSchedule", required = false) String isWeekDaysSchedule,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime timeStartEveryDay,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime timeEndEveryDay,
                         @RequestParam(required = false, value = "monED") String monED,
                         @RequestParam(required = false, value = "tueED") String tueED,
                         @RequestParam(required = false, value = "wedED") String wedED,
                         @RequestParam(required = false, value = "thuED") String thuED,
                         @RequestParam(required = false, value = "friED") String friED,
                         @RequestParam(required = false, value = "monday") String monday,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime timeStartMon,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime timeEndMon,
                         @RequestParam(required = false, value = "tuesday") String tuesday,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime timeStartTue,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime timeEndTue,
                         @RequestParam(required = false, value = "wednesday") String wednesday,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime timeStartWed,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime timeEndWed,
                         @RequestParam(required = false, value = "thursday") String thursday,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime timeStartThu,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime timeEndThu,
                         @RequestParam(required = false, value = "friday") String friday,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime timeStartFri,
                         @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime timeEndFri,
                         @RequestParam int duration,
                         @RequestParam String login,
                         @RequestParam String password,
                         HttpServletRequest request) throws HospitalException {
        LoginResponse loginResponse = (LoginResponse) request.getSession().getAttribute("login");
        if (loginResponse == null || !loginResponse.getUserResponse().getUserType().equals(UserType.ADMIN)) {
            return "403";
        }
        WeekScheduleRequest weekScheduleRequest = null;
        WeekDaysScheduleRequest weekDaysScheduleRequest = null;
        if (isWeekDaysSchedule == null) {
            List<String> weekDays = new ArrayList<>();
            if (monED != null)
                weekDays.add(monED);
            if (tueED != null)
                weekDays.add(tueED);
            if (wedED != null)
                weekDays.add(wedED);
            if (thuED != null)
                weekDays.add(thuED);
            if (friED != null)
                weekDays.add(friED);
            weekScheduleRequest = new WeekScheduleRequest(timeStartEveryDay, timeEndEveryDay, weekDays);
        } else {
            List<AppointmentRequest> appointmentRequests = new ArrayList<>();
            if (monday != null) {
                appointmentRequests.add(new AppointmentRequest(monday, timeStartMon, timeEndMon));
            }
            if (tuesday != null) {
                appointmentRequests.add(new AppointmentRequest(tuesday, timeStartTue, timeEndTue));
            }
            if (wednesday != null) {
                appointmentRequests.add(new AppointmentRequest(wednesday, timeStartWed, timeEndWed));
            }
            if (thursday != null) {
                appointmentRequests.add(new AppointmentRequest(thursday, timeStartThu, timeEndThu));
            }
            if (friday != null) {
                appointmentRequests.add(new AppointmentRequest(friday, timeStartFri, timeEndFri));
            }
            weekDaysScheduleRequest = new WeekDaysScheduleRequest(appointmentRequests);
        }
        CreateDoctorRequest doctorRequest = new CreateDoctorRequest(dateStart, dateEnd, weekScheduleRequest, weekDaysScheduleRequest, duration, firstName, lastName, patronymic, speciality, room, login, password);
        doctorService.create(loginResponse.getSessionId(), doctorRequest);
        return "redirect:/main";
    }

    @GetMapping("byDoctor")
    public String getAll(Model model, @RequestParam(required = false) String date) throws HospitalException {
        String emptyDate = "";
        model.addAttribute("datee", emptyDate);
        if (StringUtils.hasLength(date)){
            model.addAttribute("datee", date);
        }
        model.addAttribute("doctors", doctorService.getAll());
        return "doctorList";
    }

    @GetMapping("bySpeciality")
    public String getAllSpecialities(Model model) throws HospitalException {
        model.addAttribute("specialities", doctorService.getAllSpecialities());
        return "specialityList";
    }

    @GetMapping("doctorsBySpeciality")
    public String getDoctorsBySpeciality(Model model, @RequestParam String speciality, HttpServletRequest request) throws HospitalException {
        if(request.getSession().getAttribute("login") == null) {
            return "login";
        }
        model.addAttribute("doctors", doctorService.getDoctorsBySpeciality(speciality));
        return "doctorsBySpecialityList";
    }

    @GetMapping(value = "doctorTickets/{id}")
    public String get(@PathVariable(value = "id") int doctorId,
                      @RequestParam(name = "schedule", required = false, defaultValue = "yes") String schedule,
                      @RequestParam(name = "dateStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateStart,
                      @RequestParam(name = "dateEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEnd,
                      Model model,
                      HttpServletRequest request) throws HospitalException {
        if(request.getSession().getAttribute("login") == null) {
            return "login";
        }
        LoginResponse loginResponse = (LoginResponse) request.getSession().getAttribute("login");
        Doctor doctor = doctorService.get(loginResponse.getSessionId(), doctorId, schedule, dateStart, dateEnd);
        model.addAttribute("doctor", doctor);
        model.addAttribute("dateStart", dateStart);
        model.addAttribute("dateEnd", dateEnd);
        return "tickets";
    }

//    @PutMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//    public DoctorResponse updateSchedule(@CookieValue("JAVASESSIONID") String sessionId,
//                                         @PathVariable(value = "id") int doctorId, @Valid @RequestBody UpdateScheduleRequest request) throws HospitalException {
//        return doctorService.update(sessionId, doctorId, request);
//    }
//
//    @DeleteMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
//    public EmptyResponse delete(@CookieValue("JAVASESSIONID") String sessionId,
//                                @PathVariable(value = "id") int doctorId, @Valid @RequestBody DeleteDoctorRequest request) throws HospitalException {
//        return doctorService.delete(sessionId, doctorId, request.getDate());
//    }

}
