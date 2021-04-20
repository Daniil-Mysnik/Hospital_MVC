package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Patient;

public interface PatientDAO {

    Patient save(Patient patient) throws HospitalException;

    Patient getById(int id) throws HospitalException;

    void delete(Patient patient) throws HospitalException;

    void update(Patient patient) throws HospitalException;

}
