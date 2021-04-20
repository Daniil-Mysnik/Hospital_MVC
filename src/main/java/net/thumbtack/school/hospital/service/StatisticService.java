package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.converter.DoctorConverter;
import net.thumbtack.school.hospital.converter.PatientConverter;
import net.thumbtack.school.hospital.converter.SchedulesConverter;
import net.thumbtack.school.hospital.converter.TicketConverter;
import net.thumbtack.school.hospital.dao.DoctorDAO;
import net.thumbtack.school.hospital.dao.PatientDAO;
import net.thumbtack.school.hospital.dao.TicketDAO;
import net.thumbtack.school.hospital.dao.UserDAO;
import net.thumbtack.school.hospital.dto.response.DoctorResponse;
import net.thumbtack.school.hospital.dto.response.PatientResponse;
import net.thumbtack.school.hospital.dto.response.ScheduleResponse;
import net.thumbtack.school.hospital.dto.response.TicketResponse;
import net.thumbtack.school.hospital.exceptions.HospitalErrorCode;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.User;
import net.thumbtack.school.hospital.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class StatisticService extends ServiceBase {
    private final DoctorDAO doctorDAO;
    private final UserDAO userDAO;
    private final TicketDAO ticketDAO;
    private final PatientDAO patientDAO;
    private final SchedulesConverter schedulesConverter;
    private final DoctorConverter doctorConverter;
    private final TicketConverter ticketConverter;
    private final PatientConverter patientConverter;
    private final UserValidator userValidator;

    @Autowired
    public StatisticService(DoctorDAO doctorDAO,
                            TicketDAO ticketDAO,
                            TicketDAO ticketDAO1,
                            PatientDAO patientDAO,
                            UserDAO userDAO,
                            SchedulesConverter schedulesConverter,
                            DoctorConverter doctorConverter,
                            TicketConverter ticketConverter,
                            PatientConverter patientConverter,
                            UserValidator userValidator) {
        super(ticketDAO);
        this.doctorDAO = doctorDAO;
        this.ticketDAO = ticketDAO1;
        this.patientDAO = patientDAO;
        this.userDAO = userDAO;
        this.schedulesConverter = schedulesConverter;
        this.doctorConverter = doctorConverter;
        this.ticketConverter = ticketConverter;
        this.patientConverter = patientConverter;
        this.userValidator = userValidator;
    }

    public DoctorResponse getDoctorWithFreeTickets(int doctorId, String schedule, LocalDate dateStart, LocalDate dateEnd) throws HospitalException {
        Doctor doctor = doctorDAO.getById(doctorId);
        if (dateStart == null)
            dateStart = LocalDate.now();
        if (dateEnd == null)
            dateEnd = dateStart.plusMonths(2);
        List<DaySchedule> daySchedules = new ArrayList<>();
        for (DaySchedule daySchedule : doctor.getDayScheduleList()) {
            if (daySchedule.getDate().plusDays(1).isAfter(dateStart) && daySchedule.getDate().minusDays(1).isBefore(dateEnd)) {
                daySchedules.add(daySchedule);
            }
        }
        doctor.setDayScheduleList(schedulesWithFreeAppointments(daySchedules));

        if (schedule.equals("no")) {
            int count = doctor.getDayScheduleList().stream().map(DaySchedule::getAppointments).mapToInt(Collection::size).sum();
            return doctorConverter.inflateResponseByTermWithCount(doctor, count);
        } else if (schedule.equals("yes")) {
            List<ScheduleResponse> scheduleResponses = schedulesConverter.inflateResponses(doctor.getDayScheduleList());
            if (scheduleResponses.size() > 0) {
                return doctorConverter.inflateResponse(doctor, scheduleResponses);
            }
        } else {
            throw new HospitalException(HospitalErrorCode.BAD_REQUEST);
        }

        return doctorConverter.inflateResponseWithoutSchedule(doctor);
    }

    public List<DoctorResponse> getAllDoctorsWithFreeTickets(String schedule, LocalDate dateStart, LocalDate dateEnd) throws HospitalException {
        List<DoctorResponse> responses = new ArrayList<>();
        List<Integer> doctorIds = doctorDAO.getAllIds();
        for (Integer id : doctorIds) {
            responses.add(getDoctorWithFreeTickets(id, schedule, dateStart, dateEnd));
        }
        return responses;
    }

    public PatientResponse getPatientWithHisAppointments(String sessionId, int patientId, String tickets, LocalDate dateStart, LocalDate dateEnd) throws HospitalException {
        User user = userDAO.getBySessionId(sessionId);
        userValidator.checkUserNotPatient(user);
        if (dateStart == null)
            dateStart = LocalDate.now();
        if (dateEnd == null)
            dateEnd = dateStart.plusMonths(2);
        List<TicketResponse> ticketResponses = ticketConverter.inflateResponses(ticketDAO.getPatientTickets(patientId, dateStart, dateEnd));
        Patient patient = patientDAO.getById(patientId);
        if (tickets.equals("no")) {
            return patientConverter.inflateWithCountOfBusyAppointmentsResponse(patient, ticketResponses.size());
        } else if (tickets.equals("yes")) {
            return patientConverter.inflateWithBusyTicketsResponse(patient, ticketResponses);
        } else {
            throw new HospitalException(HospitalErrorCode.BAD_REQUEST);
        }
    }

}
