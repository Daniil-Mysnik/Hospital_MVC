package net.thumbtack.school.hospital.converter;

import net.thumbtack.school.hospital.dto.request.CreateDoctorRequest;
import net.thumbtack.school.hospital.dto.response.CountOfFreeAppointmentsResponse;
import net.thumbtack.school.hospital.dto.response.DoctorWithScheduleResponse;
import net.thumbtack.school.hospital.dto.response.DoctorWithoutScheduleResponse;
import net.thumbtack.school.hospital.dto.response.ScheduleResponse;
import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.DaySchedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DoctorConverter {
    private final SchedulesConverter schedulesConverter;

    @Autowired
    public DoctorConverter(SchedulesConverter schedulesConverter) {
        this.schedulesConverter = schedulesConverter;
    }

    public Doctor inflateEntity(CreateDoctorRequest request) {
        List<DaySchedule> daySchedules = schedulesConverter.inflateEntities(request);
        return new Doctor(request.getFirstName(), request.getLastName(), request.getPatronymic(), request.getLogin(), request.getPassword(), request.getSpeciality(), request.getRoom(), daySchedules);
    }

    public DoctorWithScheduleResponse inflateResponse(Doctor doctor, List<ScheduleResponse> scheduleResponses) {
        return new DoctorWithScheduleResponse(doctor.getId(), doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(), doctor.getSpeciality(), doctor.getRoom(), scheduleResponses);
    }

    public DoctorWithoutScheduleResponse inflateResponseWithoutSchedule(Doctor doctor) {
        return new DoctorWithoutScheduleResponse(doctor.getId(), doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(), doctor.getSpeciality(), doctor.getRoom());
    }

    public CountOfFreeAppointmentsResponse inflateResponseByTermWithCount(Doctor doctor, int count) {
        return new CountOfFreeAppointmentsResponse(doctor.getId(), doctor.getFirstName(), doctor.getLastName(), doctor.getPatronymic(), doctor.getSpeciality(), doctor.getRoom(), count);
    }

}
