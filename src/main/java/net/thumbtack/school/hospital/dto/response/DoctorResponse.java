package net.thumbtack.school.hospital.dto.response;

import net.thumbtack.school.hospital.model.UserType;

public class DoctorResponse extends DoctorWithoutRoomResponse {
    private String room;

    public DoctorResponse() {
    }

    public DoctorResponse(int id, String firstName, String lastName, String patronymic, UserType userType, String speciality, String room) {
        super(id, firstName, lastName, patronymic, userType, speciality);
        this.room = room;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
