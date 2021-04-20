package net.thumbtack.school.hospital.dto.request;

import net.thumbtack.school.hospital.validator.BlankOrPattern;
import net.thumbtack.school.hospital.validator.MaxLength;
import net.thumbtack.school.hospital.validator.PasswordLength;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UpdateUserRequest {
    @NotBlank(message = "First name can't be empty")
    @NotNull(message = "First name can't be null")
    @Pattern(regexp = "[а-яА-Я\\s]+[-]?[а-яА-Я\\s]+", message = "First name should contain only Russian letters, spaces and dash (dash can only be in the middle).")
    @MaxLength(message = "First name is too long")
    private String firstName;

    @NotBlank(message = "Last name can't be empty")
    @NotNull(message = "Last name can't be null")
    @Pattern(regexp = "[а-яА-Я\\s]+[-]?[а-яА-Я\\s]+", message = "Last name should contain only Russian letters, spaces and dash (dash can only be in the middle).")
    @MaxLength(message = "Last name is too long")
    private String lastName;

    @BlankOrPattern(regexp = "[а-яА-Я\\s]+[-]?[а-яА-Я\\s]+", message = "Patronymic should contain only Russian letters, spaces and dash (dash can only be in the middle).")
    @MaxLength(message = "Patronymic is too long")
    private String patronymic;

    @NotBlank(message = "Password can't be empty")
    @NotNull(message = "Password can't be null")
    @PasswordLength(message = "Password is too short")
    @MaxLength(message = "Password is too long")
    private String oldPassword;

    @NotBlank(message = "Password can't be empty")
    @NotNull(message = "Password can't be null")
    @PasswordLength(message = "Password is too short")
    @MaxLength(message = "Password is too long")
    private String newPassword;

    public UpdateUserRequest() {
    }

    public UpdateUserRequest(String firstName, String lastName, String patronymic, String oldPassword, String newPassword) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.oldPassword = oldPassword;
        this.newPassword = newPassword;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

}
