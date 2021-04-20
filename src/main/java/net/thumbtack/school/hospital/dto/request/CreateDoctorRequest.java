package net.thumbtack.school.hospital.dto.request;

import net.thumbtack.school.hospital.validator.BlankOrPattern;
import net.thumbtack.school.hospital.validator.MaxLength;
import net.thumbtack.school.hospital.validator.PasswordLength;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

public class CreateDoctorRequest extends ScheduleRequest {
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

    @NotBlank(message = "Speciality name can't be empty")
    @NotNull(message = "Speciality can't be null")
    private String speciality;

    @NotBlank(message = "Room name can't be empty")
    @NotNull(message = "Room can't be null")
    private String room;

    @NotBlank(message = "Login can't be empty")
    @NotNull(message = "Login can't be null")
    @MaxLength(message = "Login is too long")
    @Pattern(regexp = "[a-zA-Zа-яА-Я0-9]+", message = "Login must contains only Russian and English letters and numbers")
    private String login;

    @NotBlank(message = "Password can't be empty")
    @NotNull(message = "Password can't be null")
    @PasswordLength(message = "Password is too short")
    @MaxLength(message = "Password is too long")
    private String password;

    public CreateDoctorRequest() {
    }

    public CreateDoctorRequest(LocalDate dateStart, LocalDate dateEnd, WeekScheduleRequest weekSchedule, WeekDaysScheduleRequest weekDaysSchedule, int duration, String firstName, String lastName, String patronymic, String speciality, String room, String login, String password) {
        super(dateStart, dateEnd, weekSchedule, weekDaysSchedule, duration);
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.speciality = speciality;
        this.room = room;
        this.login = login;
        this.password = password;
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

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
