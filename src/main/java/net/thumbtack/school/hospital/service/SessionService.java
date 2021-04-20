package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.converter.AdminConverter;
import net.thumbtack.school.hospital.converter.DoctorConverter;
import net.thumbtack.school.hospital.converter.PatientConverter;
import net.thumbtack.school.hospital.converter.SchedulesConverter;
import net.thumbtack.school.hospital.dao.*;
import net.thumbtack.school.hospital.dto.request.LoginRequest;
import net.thumbtack.school.hospital.dto.response.*;
import net.thumbtack.school.hospital.exceptions.HospitalErrorCode;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.*;
import net.thumbtack.school.hospital.validator.UserValidator;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class SessionService extends ServiceBase {
    private final SessionDAO sessionDAO;
    private final UserDAO userDAO;
    private final AdminDAO adminDAO;
    private final DoctorDAO doctorDAO;
    private final PatientDAO patientDAO;
    private final SchedulesConverter schedulesConverter;
    private final AdminConverter adminConverter;
    private final DoctorConverter doctorConverter;
    private final PatientConverter patientConverter;
    private final UserValidator userValidator;

    public SessionService(SessionDAO sessionDAO,
                          UserDAO userDAO,
                          AdminDAO adminDAO,
                          DoctorDAO doctorDAO,
                          PatientDAO patientDAO,
                          TicketDAO ticketDAO, SchedulesConverter schedulesConverter,
                          AdminConverter adminConverter,
                          DoctorConverter doctorConverter,
                          PatientConverter patientConverter,
                          UserValidator userValidator) {
        super(ticketDAO);
        this.sessionDAO = sessionDAO;
        this.userDAO = userDAO;
        this.adminDAO = adminDAO;
        this.doctorDAO = doctorDAO;
        this.patientDAO = patientDAO;
        this.schedulesConverter = schedulesConverter;
        this.adminConverter = adminConverter;
        this.doctorConverter = doctorConverter;
        this.patientConverter = patientConverter;
        this.userValidator = userValidator;
    }

    public LoginResponse create(LoginRequest request) throws HospitalException {
        User user = userDAO.getByLogin(request.getLogin());
        userValidator.checkUserExist(user);
        userValidator.checkPasswordMatch(user.getPassword(), request.getPassword());
        Session session = sessionDAO.getByUserId(user.getId());
        if (session != null) {
            sessionDAO.delete(session);
        }
        session = sessionDAO.save(new Session(UUID.randomUUID().toString(), user));
        return new LoginResponse(session.getSessionId(), getInfo(session.getSessionId()));
    }

    public UserResponse getInfo(String sessionId) throws HospitalException {
        Session session = sessionDAO.getById(sessionId);
        User user = session.getUser();
        switch (user.getUserType()) {
            case ADMIN:
                Admin admin = adminDAO.getById(user.getId());
                return adminConverter.inflateResponse(admin);

            case DOCTOR:
                Doctor doctor = doctorDAO.getById(user.getId());
                doctor.setDayScheduleList(schedulesForInflate(doctor));
                return doctorConverter.inflateResponse(doctor, schedulesConverter.inflateResponses(doctor.getDayScheduleList()));

            case PATIENT:
                Patient patient = patientDAO.getById(user.getId());
                return patientConverter.inflateResponse(patient);

            default:
                throw new HospitalException(HospitalErrorCode.UNKNOWN_USER_TYPE);
        }
    }

    public EmptyResponse delete(String sessionId) throws HospitalException {
        Session session = sessionDAO.getById(sessionId);
        sessionDAO.delete(session);
        return new EmptyResponse();
    }

}
