package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dao.*;
import net.thumbtack.school.hospital.dto.request.CommissionRequest;
import net.thumbtack.school.hospital.dto.response.CommissionResponse;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.Ticket;
import net.thumbtack.school.hospital.model.User;
import net.thumbtack.school.hospital.validator.CommissionValidator;
import net.thumbtack.school.hospital.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommissionService {
    private final UserDAO userDAO;
    private final DoctorDAO doctorDAO;
    private final AppointmentDAO appointmentDAO;
    private final ScheduleDAO scheduleDAO;
    private final PatientDAO patientDAO;
    private final CommissionDAO commissionDAO;
    private final UserValidator userValidator;
    private final CommissionValidator commissionValidator;

    @Autowired
    public CommissionService(UserDAO userDAO, DoctorDAO doctorDAO, AppointmentDAO appointmentDAO, ScheduleDAO scheduleDAO, PatientDAO patientDAO, CommissionDAO commissionDAO, UserValidator userValidator, CommissionValidator commissionValidator) {
        this.userDAO = userDAO;
        this.doctorDAO = doctorDAO;
        this.appointmentDAO = appointmentDAO;
        this.scheduleDAO = scheduleDAO;
        this.patientDAO = patientDAO;
        this.commissionDAO = commissionDAO;
        this.userValidator = userValidator;
        this.commissionValidator = commissionValidator;
    }

    public CommissionResponse create(String sessionId, CommissionRequest request) throws HospitalException {
        User user = userDAO.getBySessionId(sessionId);
        userValidator.checkUserIsDoctor(user);
        patientDAO.getById(request.getPatientId());
        List<Integer> doctorIds = request.getDoctorIds();
        if (doctorIds.contains(user.getId())) {
            doctorIds.remove(Integer.valueOf(user.getId()));
        }
        request.getDoctorIds().add(0, user.getId());
        List<String> rooms = new ArrayList<>();
        for (int doctorId : doctorIds) {
            rooms.add(doctorDAO.getRoomByDoctorId(doctorId));
        }
        commissionValidator.checkContainsRoom(rooms, request.getRoom());
        List<Appointment> allAppointments = new ArrayList<>();
        for (int doctorId : doctorIds) {
            LocalTime timeEnd = request.getTime().plusMinutes(request.getDuration());
            List<Appointment> appointments = appointmentDAO.getAppointmentsForCommission(request.getDate(), doctorId, request.getTime(), timeEnd);
            commissionValidator.checkAppointmentTimeBeforeWorking(request.getTime(), appointments.get(0).getTime());
            int duration = scheduleDAO.getDuration(doctorId, request.getDate());
            commissionValidator.checkAppointmentTimeAfterWorking(timeEnd, duration, appointments.get(appointments.size() - 1).getTime());
            allAppointments.addAll(appointments);
        }
        Ticket ticket = new Ticket(doctorIds, request.getDate(), request.getTime());
        commissionDAO.insert(request.getPatientId(), request.getDate(), request.getTime(), request.getDuration(), allAppointments, ticket);
        return new CommissionResponse(ticket.getNumber(), request.getPatientId(), doctorIds, request.getRoom(), request.getDate(), request.getTime(), request.getDuration());
    }

}
