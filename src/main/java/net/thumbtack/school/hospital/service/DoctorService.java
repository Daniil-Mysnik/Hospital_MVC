package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.converter.DoctorConverter;
import net.thumbtack.school.hospital.converter.SchedulesConverter;
import net.thumbtack.school.hospital.dao.DoctorDAO;
import net.thumbtack.school.hospital.dao.ScheduleDAO;
import net.thumbtack.school.hospital.dao.TicketDAO;
import net.thumbtack.school.hospital.dao.UserDAO;
import net.thumbtack.school.hospital.dto.request.CreateDoctorRequest;
import net.thumbtack.school.hospital.dto.request.UpdateScheduleRequest;
import net.thumbtack.school.hospital.dto.response.DoctorResponse;
import net.thumbtack.school.hospital.dto.response.DoctorWithScheduleResponse;
import net.thumbtack.school.hospital.dto.response.EmptyResponse;
import net.thumbtack.school.hospital.dto.response.ScheduleResponse;
import net.thumbtack.school.hospital.exceptions.HospitalErrorCode;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.*;
import net.thumbtack.school.hospital.validator.AppointmentValidator;
import net.thumbtack.school.hospital.validator.UserValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DoctorService extends ServiceBase {
    private final UserDAO userDAO;
    private final DoctorDAO doctorDAO;
    private final ScheduleDAO scheduleDAO;
    private final TicketDAO ticketDAO;
    private final DoctorConverter doctorConverter;
    private final SchedulesConverter schedulesConverter;
    private final UserValidator userValidator;
    private final AppointmentValidator appointmentValidator;

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorService.class);

    @Autowired
    public DoctorService(UserDAO userDAO, DoctorDAO doctorDAO,
                         ScheduleDAO scheduleDAO, TicketDAO ticketDAO, DoctorConverter doctorConverter,
                         UserValidator userValidator,
                         SchedulesConverter schedulesConverter,
                         AppointmentValidator appointmentValidator) {
        super(ticketDAO);
        this.userDAO = userDAO;
        this.doctorDAO = doctorDAO;
        this.scheduleDAO = scheduleDAO;
        this.ticketDAO = ticketDAO;
        this.doctorConverter = doctorConverter;
        this.userValidator = userValidator;
        this.schedulesConverter = schedulesConverter;
        this.appointmentValidator = appointmentValidator;
    }

    public DoctorWithScheduleResponse create(String sessionId, CreateDoctorRequest request) throws HospitalException {
        userValidator.checkUserInSessionAdmin(sessionId);
        scheduleDAO.checkSchedulesByRoom(request.getRoom());
        Doctor doctor = doctorConverter.inflateEntity(request);
        for (DaySchedule daySchedule : doctor.getDayScheduleList()) {
            daySchedule.setDoctor(doctor);
            for (Appointment appointment : daySchedule.getAppointments()) {
                appointment.setDaySchedule(daySchedule);
            }
        }
        Doctor doctorFromDB = doctorDAO.save(doctor);
        List<ScheduleResponse> scheduleResponses = schedulesConverter.inflateResponses(doctorFromDB.getDayScheduleList());
        return doctorConverter.inflateResponse(doctorFromDB, scheduleResponses);
    }

    public Doctor get(String sessionId,
                              int doctorId,
                              String schedule,
                              LocalDate dateStart,
                              LocalDate dateEnd) throws HospitalException {
        Doctor doctor = doctorDAO.getById(doctorId);
        doctor.setDayScheduleList(schedulesForInflate(doctor));

        if (dateStart == null)
            dateStart = LocalDate.now();
        if (dateEnd == null)
            dateEnd = dateStart.plusDays(6);
        LocalDate finalDateStart = dateStart.minusDays(1);
        LocalDate finalDateEnd = dateEnd.plusDays(1);
        List<DaySchedule> dayScheduleList = doctor.getDayScheduleList().stream()
                                                                       .filter(daySchedule -> daySchedule.getDate().isAfter(finalDateStart)
                                                                               && daySchedule.getDate().isBefore(finalDateEnd))
                                                                       .collect(Collectors.toList());
        doctor.setDayScheduleList(dayScheduleList);
        return doctor;
//        if (schedule.equals("no")) {
//            return doctor;
//        } else if(schedule.equals("yes")) {
//            List<ScheduleResponse> scheduleResponses = schedulesConverter.inflateResponsesByTerm(doctor.getDayScheduleList(),
//                                                                                                 user,
//                                                                                                 dateStart,
//                                                                                                 dateEnd);
//            if (scheduleResponses.size() > 0) {
//                return doctorConverter.inflateResponse(doctor, scheduleResponses);
//            }
//        } else {
//            throw new HospitalException(HospitalErrorCode.BAD_REQUEST);
//        }
//        return doctorConverter.inflateResponseWithoutSchedule(doctor);
    }

//    public List<DoctorResponse> getAll() throws HospitalException {
//        List<Doctor> doctors = doctorDAO.getAll();
//        List<DoctorResponse> doctorResponses = new ArrayList<>();
//        for (Doctor doctor : doctors) {
//            doctorResponses.add(doctorConverter.inflateResponseWithoutSchedule(doctor));
//        }
//        return doctorResponses;
//    }

    public List<Doctor> getAll() throws HospitalException {
        return doctorDAO.getAll();
    }

    public List<String> getAllSpecialities() throws HospitalException {
        return doctorDAO.getAllSpecialities();
    }

//    public List<DoctorResponse> getRsBySpeciality(String sessionId,
//                                                  String schedule,
//                                                  String speciality,
//                                                  LocalDate dateStart,
//                                                  LocalDate dateEnd) throws HospitalException {
//        List<DoctorResponse> responses = new ArrayList<>();
//        if (!speciality.equals("")) {
//            List<Integer> listId = doctorDAO.getIdsBySpeciality(speciality);
//            for (int id : listId) {
//                responses.add(get(sessionId, id, schedule, dateStart, dateEnd));
//            }
//        } else {
//            List<Integer> allId = doctorDAO.getAllIds();
//            for (int id : allId) {
//                responses.add(get(sessionId, id, schedule, dateStart, dateEnd));
//            }
//        }
//        return responses;
//    }

    public List<Doctor> getDoctorsBySpeciality(String speciality) throws HospitalException {
        return doctorDAO.getBySpeciality(speciality);
    }

    public DoctorResponse update(String sessionId, int doctorId, UpdateScheduleRequest request) throws HospitalException {
        userValidator.checkUserInSessionAdmin(sessionId);
        Doctor doctor = doctorDAO.getById(doctorId);
        List<DaySchedule> daySchedules = doctor.getDayScheduleList();
        List<DaySchedule> oldDaySchedule = new ArrayList<>();
        for (DaySchedule daySchedule : daySchedules) {
            if (daySchedule.getDate().minusDays(1).isBefore(request.getDateEnd())
                    && daySchedule.getDate().plusDays(1).isAfter(request.getDateStart())) {
                oldDaySchedule.add(daySchedule);
            }
        }
        for (DaySchedule daySchedule : oldDaySchedule) {
            for (Appointment appointment : daySchedule.getAppointments()) {
                appointmentValidator.checkAppointmentFree(appointment);
            }
            daySchedules.remove(daySchedule);
        }
        List<DaySchedule> newDaySchedule = schedulesConverter.inflateEntities(request);
        for (DaySchedule daySchedule : newDaySchedule) {
            daySchedule.setDoctor(doctor);
            for (Appointment appointment : daySchedule.getAppointments()) {
                appointment.setDaySchedule(daySchedule);
            }
        }
        scheduleDAO.updateSchedule(doctor, oldDaySchedule, newDaySchedule);
        daySchedules.addAll(newDaySchedule);
        List<ScheduleResponse> scheduleResponses = schedulesConverter.inflateResponses(daySchedules);
        return doctorConverter.inflateResponse(doctor, scheduleResponses);
    }

    public EmptyResponse delete(String sessionId, int doctorId, LocalDate date) throws HospitalException {
        userValidator.checkUserInSessionAdmin(sessionId);
        List<Ticket> tickets = ticketDAO.getTicketByDoctorAndDate(doctorId, date);
        if (tickets.size() > 0) {
            for (Ticket ticket : tickets) {
                ticketDAO.deleteTicket(ticket.getNumber());
                LOGGER.info("Send letter on email: {} 'The appointment with ticket {} has been cancelled'", ticket.getPatient().getEmail(), ticket.getNumber());
                LOGGER.info("Send sms on phone: {} 'The appointment with ticket {} has been cancelled'", ticket.getPatient().getPhone(), ticket.getNumber());
            }
        }
        doctorDAO.deleteByDate(doctorId, date);
        return new EmptyResponse();
    }

}
