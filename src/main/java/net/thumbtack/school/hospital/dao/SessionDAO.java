package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Session;


public interface SessionDAO {

    Session save(Session session) throws HospitalException;

    Session getById(String id) throws HospitalException;

    Session getByUserId(int id) throws HospitalException;

    void delete(Session session) throws HospitalException;

}
