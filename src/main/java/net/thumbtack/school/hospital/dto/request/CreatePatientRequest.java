package net.thumbtack.school.hospital.dto.request;

import net.thumbtack.school.hospital.validator.MaxLength;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class CreatePatientRequest extends CreateUserRequest {
    @NotBlank(message = "Email can't be empty")
    @NotNull(message = "Email can't be null")
    @Email(message = "Invalid email format")
    @MaxLength(message = "Email is too long")
    private String email;

    @NotBlank(message = "Address can't be empty")
    @NotNull(message = "Address can't be null")
    private String address;

    @NotBlank(message = "Phone can't be empty")
    @NotNull(message = "Phone can't be null")
    @Pattern(regexp = "(\\+7|8)[-]?\\d{3}[-]?\\d{3}[-]?(\\d){2}[-]?(\\d){2}", message = "Invalid phone format")
    private String phone;

    public CreatePatientRequest() {
    }

    public CreatePatientRequest(String firstName, String lastName, String patronymic, String login, String password, String email, String address, String phone) {
        super(firstName, lastName, patronymic, login, password);
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

}
