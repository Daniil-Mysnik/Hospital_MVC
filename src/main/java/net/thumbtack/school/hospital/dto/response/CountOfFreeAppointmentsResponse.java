package net.thumbtack.school.hospital.dto.response;

import net.thumbtack.school.hospital.model.UserType;

public class CountOfFreeAppointmentsResponse extends DoctorResponse {
    private int count;

    public CountOfFreeAppointmentsResponse() {
    }

    public CountOfFreeAppointmentsResponse(int id, String firstName, String lastName, String patronymic, String speciality, String room, int count) {
        super(id, firstName, lastName, patronymic, UserType.DOCTOR, speciality, room);
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}
