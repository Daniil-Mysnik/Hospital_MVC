package net.thumbtack.school.hospital.dto.response;

import net.thumbtack.school.hospital.model.UserType;

public class UserResponse {
    private int id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private UserType userType;

    public UserResponse() {
    }

    public UserResponse(int id, String firstName, String lastName, String patronymic, UserType userType) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.userType = userType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
