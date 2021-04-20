package net.thumbtack.school.hospital.dao;

import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.User;

public interface UserDAO {

    User getById(int id) throws HospitalException;

    User getByLogin(String login) throws HospitalException;

    User getBySessionId(String id) throws HospitalException;

}
