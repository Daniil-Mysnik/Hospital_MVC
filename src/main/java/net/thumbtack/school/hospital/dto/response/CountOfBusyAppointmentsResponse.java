package net.thumbtack.school.hospital.dto.response;

import net.thumbtack.school.hospital.model.UserType;

public class CountOfBusyAppointmentsResponse extends PatientResponse {
    int count;

    public CountOfBusyAppointmentsResponse() {
    }

    public CountOfBusyAppointmentsResponse(int id, String firstName, String lastName, String patronymic, String email, String address, String phone, UserType userType, int count) {
        super(id, firstName, lastName, patronymic, email, address, userType, phone);
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
