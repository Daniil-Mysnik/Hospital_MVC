package net.thumbtack.school.hospital.mapper;

import net.thumbtack.school.hospital.model.Patient;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface PatientMapper {
    @Insert("INSERT INTO patient (user_id, email, address, phone) " +
            "SELECT id, #{email}, #{address}, #{phone} FROM user WHERE login = #{login}")
    void insert(Patient patient);

    @Select("SELECT user.id, firstName, lastName, patronymic, login, password, email, address, phone, userType FROM patient " +
            "JOIN user ON patient.user_id = user.id WHERE user_id = #{id}")
    Patient getById(int patientId);

    @Update("UPDATE patient JOIN user ON patient.user_id = user.id " +
            "SET firstName = #{firstName}, lastName = #{lastName}, patronymic = #{patronymic}, " +
            "login = #{login}, password = #{password}, email = #{email}, address = #{address}, phone = #{phone} " +
            "WHERE user_id = #{id}")
    void update(Patient patient);

    @Delete("DELETE FROM user WHERE id = #{id}")
    void delete(Patient patient);

}
