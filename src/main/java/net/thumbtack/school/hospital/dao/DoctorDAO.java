package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Doctor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface DoctorDAO {

    Doctor save(Doctor doctor) throws HospitalException;

    Doctor getById(int id) throws HospitalException;

    List<Doctor> getBySpeciality(String speciality) throws HospitalException;

    List<Doctor> getAll() throws HospitalException;

    List<Integer> getIdsBySpeciality(String speciality) throws HospitalException;

    List<Integer> getAllIds() throws HospitalException;

    List<String> getAllSpecialities() throws HospitalException;

    Doctor getRandomBySpeciality(String speciality, LocalDate date, LocalTime time) throws HospitalException;

    String getRoomByDoctorId(int doctorId) throws HospitalException;

    void update(Doctor doctor) throws HospitalException;

    void deleteByDate(int doctorId, LocalDate date) throws HospitalException;

    void delete(Doctor doctor) throws HospitalException;

}
