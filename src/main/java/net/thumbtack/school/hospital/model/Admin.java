package net.thumbtack.school.hospital.model;

import java.util.Objects;

public class Admin extends User {
    private String position;

    public Admin() {
    }

    public Admin(int id, String firstName, String lastName, String patronymic, String login, String password, String position) {
        super(id, firstName, lastName, patronymic, login, password, UserType.ADMIN);
        this.position = position;
    }

    public Admin(String firstName, String lastName, String patronymic, String login, String password, String position) {
        this(0, firstName, lastName, patronymic, login, password, position);
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Admin)) return false;
        if (!super.equals(o)) return false;
        Admin admin = (Admin) o;
        return Objects.equals(getPosition(), admin.getPosition());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getPosition());
    }

}
