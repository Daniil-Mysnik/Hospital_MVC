package net.thumbtack.school.hospital.dto.response;

import net.thumbtack.school.hospital.model.UserType;

public class AdminResponse extends UserResponse {
    private String position;

    public AdminResponse() {
    }

    public AdminResponse(int id, String firstName, String lastName, String patronymic, String position, UserType userType) {
        super(id, firstName, lastName, patronymic, userType);
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}
