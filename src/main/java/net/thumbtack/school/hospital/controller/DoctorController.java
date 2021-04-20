package net.thumbtack.school.hospital.controller;

import net.thumbtack.school.hospital.dto.request.CreateDoctorRequest;
import net.thumbtack.school.hospital.dto.response.DoctorResponse;
import net.thumbtack.school.hospital.dto.response.LoginResponse;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.service.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
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

@Controller
public class DoctorController {
    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @GetMapping("registerDoctor")
    public String register() {
        return "registerDoctor";
    }

    @PostMapping("registerDoctor")
    public String create(@RequestParam String firstName,
                         @RequestParam String lastName,
                         @RequestParam String patronymic,
                         @RequestParam String login,
                         @RequestParam String password,
                         HttpServletRequest request) throws HospitalException {
        CreateDoctorRequest doctorRequest = new CreateDoctorRequest(LocalDate.now(), LocalDate.now().plusDays(100), null, null, 0, firstName, lastName, patronymic, "erwe", "2", login, password);
        LoginResponse loginResponse = (LoginResponse) request.getSession().getAttribute("login");
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
    public String getDoctorsBySpeciality(Model model, @RequestParam String speciality) throws HospitalException {
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
        LoginResponse loginResponse = (LoginResponse) request.getSession().getAttribute("login");
        Doctor doctor = doctorService.get(loginResponse.getSessionId(), doctorId, schedule, dateStart, dateEnd);
        model.addAttribute("doctor", doctor);
        model.addAttribute("dateStart", dateStart);
        model.addAttribute("dateEnd", dateEnd);
        return "tickets";
    }
//
//    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
//    public List<DoctorResponse> getBySpeciality(@CookieValue("JAVASESSIONID") String sessionId,
//                                                @RequestParam(name = "schedule", required = false, defaultValue = "no") String schedule,
//                                                @RequestParam(name = "speciality", required = false, defaultValue = "") String speciality,
//                                                @RequestParam(name = "dateStart", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateStart,
//                                                @RequestParam(name = "dateEnd", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateEnd) throws HospitalException {
//        return doctorService.getBySpeciality(sessionId, schedule, speciality, dateStart, dateEnd);
//    }
//
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
