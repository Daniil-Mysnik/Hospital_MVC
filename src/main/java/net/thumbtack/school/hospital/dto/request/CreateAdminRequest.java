package net.thumbtack.school.hospital.dto.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CreateAdminRequest extends CreateUserRequest {
    @NotBlank(message = "Position can't be empty")
    @NotNull(message = "Position can't be null")
    private String position;

    public CreateAdminRequest() {
    }

    public CreateAdminRequest(String firstName, String lastName, String patronymic, String login, String password, String position) {
        super(firstName, lastName, patronymic, login, password);
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

}
