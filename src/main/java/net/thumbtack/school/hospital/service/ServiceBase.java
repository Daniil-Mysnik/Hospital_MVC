package net.thumbtack.school.hospital.service;

import net.thumbtack.school.hospital.dao.TicketDAO;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Service
public class ServiceBase {
    private final TicketDAO ticketDAO;

    @Autowired
    public ServiceBase(TicketDAO ticketDAO) {
        this.ticketDAO = ticketDAO;
    }

    protected List<DaySchedule> schedulesForInflate(Doctor doctor) throws HospitalException {
        for (DaySchedule daySchedule: doctor.getDayScheduleList()) {
            List<Appointment> appointments = new LinkedList<>();
            for (Appointment appointment : daySchedule.getAppointments()) {
                switch (appointment.getState()) {
                    case FREE: {
                        appointments.add(appointment);
                        break;
                    }

                    case TICKET: {
                        Ticket ticket = ticketDAO.getTicket(doctor.getId(), daySchedule.getDate(), appointment.getTime());
                        appointment.setPatient(ticket.getPatient());
                        appointment.setTicket(ticket.getNumber());
                        appointments.add(appointment);
                        break;
                    }

                    case COMMISSION: {
                        Ticket ticket = ticketDAO.getCommissionTicket(doctor.getId(), daySchedule.getDate(), appointment.getTime());
                        LocalTime time = ticket.getTime();
                        boolean contains = false;
                        for (Appointment appointment1 : appointments) {
                            if (appointment1.getTime().equals(time)) {
                                contains = true;
                                break;
                            }
                        }
                        if (contains) {
                            break;
                        }
                        appointment.setTime(ticket.getTime());
                        appointment.setDuration(ticket.getDuration());
                        appointment.setPatient(ticket.getPatient());
                        appointment.setTicket(ticket.getNumber());
                        appointments.add(appointment);
                        break;
                    }
                }
            }
            daySchedule.setAppointments(new ArrayList<>(appointments));
            appointments.clear();
        }
        return doctor.getDayScheduleList();
    }

    protected List<DaySchedule> schedulesWithFreeAppointments(List<DaySchedule> daySchedules) {
        for (DaySchedule daySchedule : daySchedules) {
            List<Appointment> appointments = new LinkedList<>();
            for (Appointment appointment : daySchedule.getAppointments()) {
                if (appointment.getState().equals(State.FREE)) {
                    appointments.add(appointment);
                }
            }
            daySchedule.setAppointments(new ArrayList<>(appointments));
            appointments.clear();
        }
        return daySchedules;
    }

}
