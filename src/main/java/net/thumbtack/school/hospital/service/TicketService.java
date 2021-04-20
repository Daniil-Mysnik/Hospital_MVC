package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.converter.DoctorConverter;
import net.thumbtack.school.hospital.converter.TicketConverter;
import net.thumbtack.school.hospital.dao.AppointmentDAO;
import net.thumbtack.school.hospital.dao.DoctorDAO;
import net.thumbtack.school.hospital.dao.TicketDAO;
import net.thumbtack.school.hospital.dao.UserDAO;
import net.thumbtack.school.hospital.dto.request.TicketRequest;
import net.thumbtack.school.hospital.dto.response.*;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.*;
import net.thumbtack.school.hospital.validator.AppointmentValidator;
import net.thumbtack.school.hospital.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {
    private final UserDAO userDAO;
    private final DoctorDAO doctorDAO;
    private final TicketDAO ticketDAO;
    private final AppointmentDAO appointmentDAO;
    private final TicketConverter ticketConverter;
    private final UserValidator userValidator;
    private final AppointmentValidator appointmentValidator;
    private final DoctorConverter doctorConverter;

    @Autowired
    public TicketService(UserDAO userDAO,
                         DoctorDAO doctorDAO,
                         TicketDAO ticketDAO,
                         AppointmentDAO appointmentDAO, TicketConverter ticketConverter,
                         UserValidator userValidator,
                         AppointmentValidator appointmentValidator, DoctorConverter doctorConverter) {
        this.userDAO = userDAO;
        this.doctorDAO = doctorDAO;
        this.ticketDAO = ticketDAO;
        this.appointmentDAO = appointmentDAO;
        this.ticketConverter = ticketConverter;
        this.userValidator = userValidator;
        this.appointmentValidator = appointmentValidator;
        this.doctorConverter = doctorConverter;
    }

    public TicketResponse create(String sessionId, TicketRequest request) throws HospitalException {
        User user = userDAO.getBySessionId(sessionId);
        userValidator.checkUserIsPatient(user);
        if (request.getDoctorId() != 0) {
            Doctor doctor = doctorDAO.getById(request.getDoctorId());
            Appointment appointment = appointmentDAO.getAppointmentByTime(doctor.getId(), request.getDate(), request.getTime());
            appointmentValidator.checkAppointmentFree(appointment);
            Ticket ticket = new Ticket(doctor.getId(), request.getDate(), request.getTime());
            ticketDAO.insert(appointment.getId(), ticket, user.getId(), State.TICKET);
            DoctorWithoutScheduleResponse doctorResponse = doctorConverter.inflateResponseWithoutSchedule(doctor);
            return ticketConverter.inflateResponse(ticket.getNumber(), doctorResponse, request);
        }
        Doctor doctor = doctorDAO.getRandomBySpeciality(request.getSpeciality(), request.getDate(), request.getTime());
        Appointment appointment = appointmentDAO.getAppointmentByTime(doctor.getId(), request.getDate(), request.getTime());
        Ticket ticket = new Ticket(doctor.getId(), request.getDate(), request.getTime());
        ticketDAO.insert(appointment.getId(), ticket, user.getId(), State.TICKET);
        DoctorWithoutScheduleResponse doctorResponse = doctorConverter.inflateResponseWithoutSchedule(doctor);
        return ticketConverter.inflateResponse(ticket.getNumber(), doctorResponse, request);
    }

    public TicketListResponse getTicketList(String sessionId) throws HospitalException {
        User user = userDAO.getBySessionId(sessionId);
        userValidator.checkUserIsPatient(user);
        List<String> tickets = ticketDAO.getTicketsByPatientId(user.getId());
        List<TicketResponse> response  = new ArrayList<>();
        for (String ticket : tickets) {
            List<TicketWithOneDoctorResponse> ticketResponses = ticketDAO.getAllInfoByTicket(ticket);
            if (ticketResponses.size() == 1) {
                response.add(ticketResponses.get(0));
            } else {
                List<DoctorWithoutRoomResponse> doctorResponses = new ArrayList<>();
                String room = doctorDAO.getRoomByDoctorId(new Ticket().getDoctorsFromTicket(tickets.get(0)).get(0));
                for (TicketWithOneDoctorResponse ticketWithDoctor : ticketResponses) {
                    doctorResponses.add(new DoctorWithoutRoomResponse(ticketWithDoctor.getDoctorId(), ticketWithDoctor.getFirstName(), ticketWithDoctor.getLastName(), ticketWithDoctor.getPatronymic(), UserType.DOCTOR, ticketWithDoctor.getSpeciality()));
                }
                response.add(new TicketCommissionResponse(ticketResponses.get(0).getTicket(), room, ticketResponses.get(0).getDate(), ticketResponses.get(0).getTime(), doctorResponses));
            }
        }
        return new TicketListResponse(response);
    }

    public EmptyResponse delete(String sessionId, String ticket) throws HospitalException {
        int patientId = ticketDAO.getPatientIdByTicket(ticket);
        User user = userDAO.getBySessionId(sessionId);
        List<Integer> doctorIds = new Ticket().getDoctorsFromTicket(ticket);
        userValidator.checkUserIsAdminOrMemberOfAppointment(user, patientId, doctorIds);
        ticketDAO.deleteTicket(ticket);
        return new EmptyResponse();
    }

}