package net.thumbtack.school.hospital.dto.response;

import net.thumbtack.school.hospital.model.UserType;

public class DoctorWithoutRoomResponse extends UserResponse {
    private String speciality;

    public DoctorWithoutRoomResponse() {
    }

    public DoctorWithoutRoomResponse(int id, String firstName, String lastName, String patronymic, UserType userType, String speciality) {
        super(id, firstName, lastName, patronymic, userType);
        this.speciality = speciality;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

}
