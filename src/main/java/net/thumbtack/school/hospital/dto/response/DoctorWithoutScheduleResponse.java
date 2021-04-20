package net.thumbtack.school.hospital.dto.response;

import net.thumbtack.school.hospital.model.UserType;

public class DoctorWithoutScheduleResponse extends DoctorResponse {

    public DoctorWithoutScheduleResponse(int id, String firstName, String lastName, String patronymic, String speciality, String room) {
        super(id, firstName, lastName, patronymic, UserType.DOCTOR, speciality, room);
    }

}
