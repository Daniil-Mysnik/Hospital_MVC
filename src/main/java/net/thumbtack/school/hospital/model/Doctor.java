package net.thumbtack.school.hospital.model;

import java.util.List;
import java.util.Objects;

public class Doctor extends User {
    private String speciality;
    private String room;
    private List<DaySchedule> dayScheduleList;

    public Doctor() {
    }

    public Doctor(int id,
                  String firstName,
                  String lastName,
                  String patronymic,
                  String login,
                  String password,
                  String speciality,
                  String room,
                  List<DaySchedule> dayScheduleList) {
        super(id, firstName, lastName, patronymic, login, password, UserType.DOCTOR);
        this.speciality = speciality;
        this.room = room;
        this.dayScheduleList = dayScheduleList;
    }

    public Doctor(String firstName,
                  String lastName,
                  String patronymic,
                  String login,
                  String password,
                  String speciality,
                  String room,
                  List<DaySchedule> dayScheduleList) {
        this(0, firstName, lastName, patronymic, login, password, speciality, room, dayScheduleList);
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

    public List<DaySchedule> getDayScheduleList() {
        return dayScheduleList;
    }

    public void setDayScheduleList(List<DaySchedule> dayScheduleList) {
        this.dayScheduleList = dayScheduleList;
    }

    public int getFreeTickets() {
        return dayScheduleList.stream()
                              .map(DaySchedule::getAppointments)
                              .map(appointments -> appointments.stream()
                                                               .filter(appointment -> appointment.getState() == State.FREE)
                                                               .count())
                              .reduce(0L, Long::sum).intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(speciality, doctor.speciality) &&
                Objects.equals(room, doctor.room);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), speciality, room);
    }

}
