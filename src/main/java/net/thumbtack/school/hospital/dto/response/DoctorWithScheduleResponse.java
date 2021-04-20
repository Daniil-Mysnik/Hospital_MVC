package net.thumbtack.school.hospital.dto.response;

import net.thumbtack.school.hospital.model.UserType;

import java.util.List;

public class DoctorWithScheduleResponse extends DoctorResponse {
    private List<ScheduleResponse> schedule;

    public DoctorWithScheduleResponse() {
    }

    public DoctorWithScheduleResponse(int id, String firstName, String lastName, String patronymic, String speciality, String room, List<ScheduleResponse> schedule) {
        super(id, firstName, lastName, patronymic, UserType.DOCTOR, speciality, room);
        this.schedule = schedule;
    }

    public List<ScheduleResponse> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<ScheduleResponse> schedule) {
        this.schedule = schedule;
    }

}
