package net.thumbtack.school.hospital.converter;

import net.thumbtack.school.hospital.dto.request.AppointmentRequest;
import net.thumbtack.school.hospital.dto.request.ScheduleRequest;
import net.thumbtack.school.hospital.dto.request.WeekScheduleRequest;
import net.thumbtack.school.hospital.dto.response.AppointmentResponse;
import net.thumbtack.school.hospital.dto.response.BusyAppointmentResponse;
import net.thumbtack.school.hospital.dto.response.FreeAppointmentResponse;
import net.thumbtack.school.hospital.dto.response.ScheduleResponse;
import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

@Component
public class SchedulesConverter {
    private final PatientConverter patientConverter;
    private final AppointmentConverter appointmentConverter;

    @Autowired
    public SchedulesConverter(PatientConverter patientConverter, AppointmentConverter appointmentConverter) {
        this.patientConverter = patientConverter;
        this.appointmentConverter = appointmentConverter;
    }

    public List<DaySchedule> inflateEntities(ScheduleRequest request) {
        if (request.getWeekSchedule() == null)
            return inflateDaysEntities(request.getDateStart(), request.getDateEnd(), request.getDuration(), request.getWeekDaysSchedule().getDaySchedule());
        else
            return inflateWeekEntities(request.getDateStart(), request.getDateEnd(), request.getDuration(), request.getWeekSchedule());
    }

    private List<DaySchedule> inflateDaysEntities(LocalDate start, LocalDate end, int duration, List<AppointmentRequest> requests) {
        List<DaySchedule> daySchedules = new ArrayList<>();
        LocalDate date = start;
        while (date.isBefore(end.plusDays(1))) {
            String weekDay = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            List<Appointment> appointments = new ArrayList<>();
            for (AppointmentRequest day : requests)
                if (weekDay.equalsIgnoreCase(day.getWeekDay()))
                    appointments.addAll(appointmentConverter.getEmptyAppointments(day.getTimeStart(), day.getTimeEnd(), duration));
                LocalTime timeStart = null;
                LocalTime timeEnd = null;
                if (appointments.size() > 0) {
                    timeStart = appointments.get(0).getTime();
                    timeEnd = appointments.get(appointments.size() - 1).getTime().plusMinutes(appointments.get(0).getDuration());
                }
            daySchedules.add(new DaySchedule(date, timeStart, timeEnd, appointments));
            date = date.plusDays(1);
        }
        return daySchedules;
    }

    private List<DaySchedule> inflateWeekEntities(LocalDate start, LocalDate end, int duration, WeekScheduleRequest request) {
        List<DaySchedule> daySchedules = new ArrayList<>();
        LocalDate date = start;
        while (date.isBefore(end.plusDays(1))) {
            String weekDay = date.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            List<Appointment> appointments = new ArrayList<>();
            for (String day : request.getWeekDays())
                if (day.equalsIgnoreCase(weekDay))
                    appointments.addAll(appointmentConverter.getEmptyAppointments(request.getTimeStart(), request.getTimeEnd(), duration));
            daySchedules.add(new DaySchedule(date, request.getTimeStart(), request.getTimeEnd(), appointments));
            date = date.plusDays(1);
        }
        return daySchedules;
    }

    public List<ScheduleResponse> inflateResponses(List<DaySchedule> daySchedules) {
        Collections.sort(daySchedules);
        List<ScheduleResponse> scheduleResponses = new ArrayList<>();
        for (DaySchedule daySchedule : daySchedules) {
            if (!daySchedule.getAppointments().isEmpty()) {
                List<AppointmentResponse> appointmentResponses = new ArrayList<>();
                for (Appointment appointment : daySchedule.getAppointments()) {
                    if (appointment.getPatient() != null) {
                        appointmentResponses.add(new BusyAppointmentResponse(appointment.getTime(), patientConverter.inflateResponse(appointment.getPatient())));
                    } else {
                        appointmentResponses.add(new FreeAppointmentResponse(appointment.getTime()));
                    }
                }
                scheduleResponses.add(new ScheduleResponse(daySchedule.getDate(), appointmentResponses));
            }
        }
        return scheduleResponses;
    }

    public List<ScheduleResponse> inflateResponsesByTerm(List<DaySchedule> daySchedules, User user, LocalDate dateStart, LocalDate dateEnd) {
        List<ScheduleResponse> scheduleResponses = new ArrayList<>();
        if (dateStart == null) {
            dateStart = LocalDate.now();
        }
        if (dateEnd == null) {
            dateEnd = dateStart.plusMonths(2);
        }
        for (DaySchedule daySchedule : daySchedules) {
            if (daySchedule.getDate().isAfter(dateStart.minusDays(1)) && daySchedule.getDate().isBefore(dateEnd.plusDays(1))) {
                if (!daySchedule.getAppointments().isEmpty()) {
                    scheduleResponses.add(new ScheduleResponse(daySchedule.getDate(), appointmentConverter.inflateAppointments(daySchedule, user)));
                }
            }
        }
        return scheduleResponses;
    }

}
