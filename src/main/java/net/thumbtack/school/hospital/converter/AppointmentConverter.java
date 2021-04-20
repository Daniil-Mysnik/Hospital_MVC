package net.thumbtack.school.hospital.converter;

import net.thumbtack.school.hospital.dto.response.AppointmentResponse;
import net.thumbtack.school.hospital.dto.response.BusyAppointmentResponse;
import net.thumbtack.school.hospital.dto.response.FreeAppointmentResponse;
import net.thumbtack.school.hospital.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class AppointmentConverter {
    private final PatientConverter patientConverter;

    @Autowired
    public AppointmentConverter(PatientConverter patientConverter) {
        this.patientConverter = patientConverter;
    }

    public List<Appointment> getEmptyAppointments(LocalTime from, LocalTime to, int duration) {
        List<Appointment> result = new ArrayList<>();
        while (from.compareTo(to) < 0) {
            result.add(new Appointment(from, duration, State.FREE));
            from = from.plusMinutes(duration);
        }
        return result;
    }

    public List<AppointmentResponse> inflateAppointments(DaySchedule daySchedule, User user) {
        List<AppointmentResponse> appointmentResponses = new ArrayList<>();
        for (Appointment appointment : daySchedule.getAppointments()) {
            if (appointment.getPatient() != null && (user.getUserType() != UserType.PATIENT || user.getId() == appointment.getPatient().getId())) {
                appointmentResponses.add(new BusyAppointmentResponse(appointment.getTime(), patientConverter.inflateResponse(appointment.getPatient())));
            } else {
                appointmentResponses.add(new FreeAppointmentResponse(appointment.getTime()));
            }
        }
        return appointmentResponses;
    }

}
