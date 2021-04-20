package net.thumbtack.school.hospital.model;

import java.util.Objects;

public class Patient extends User {
    private String email;
    private String address;
    private String phone;

    public Patient() {
    }

    public Patient(int id, String firstName, String lastName, String patronymic, String login, String password, String email, String address, String phone) {
        super(id, firstName, lastName, patronymic, login, password, UserType.PATIENT);
        this.email = email;
        this.address = address;
        this.phone = phone;
    }

    public Patient(String firstName, String lastName, String patronymic, String login, String password, String email, String address, String phone) {
        this(0, firstName, lastName, patronymic, login, password, email, address, phone);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Patient patient = (Patient) o;
        return Objects.equals(email, patient.email) &&
                Objects.equals(address, patient.address) &&
                Objects.equals(phone, patient.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, address, phone);
    }

}
