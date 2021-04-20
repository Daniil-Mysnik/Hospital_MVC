package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Admin;

public interface AdminDAO {

    Admin save(Admin admin) throws HospitalException;

    Admin getById(int id) throws HospitalException;

    Admin getBySessionId(String sessionId) throws HospitalException;

    void delete(Admin admin) throws HospitalException;

    void update(Admin admin) throws HospitalException;

}
