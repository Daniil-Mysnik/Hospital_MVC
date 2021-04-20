package net.thumbtack.school.hospital.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class UpdateAdminRequest extends UpdateUserRequest {
    @NotBlank(message = "Position can't be empty")
    @NotNull(message = "Position can't be null")
    private String position;

    public UpdateAdminRequest() {
    }

    public UpdateAdminRequest(String firstName, String lastName, String patronymic, String position, String oldPassword, String newPassword) {
        super(firstName, lastName, patronymic, oldPassword, newPassword);
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}
