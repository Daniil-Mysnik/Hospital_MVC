package net.thumbtack.school.hospital.mapper;

import net.thumbtack.school.hospital.model.Admin;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface AdminMapper {
    @Insert("INSERT INTO admin (user_id, position) " +
            "SELECT id, #{position} FROM user WHERE login = #{login}")
    void insert(Admin admin);

    @Select("SELECT user.id, firstName, lastName, patronymic, position, login, password, userType FROM admin " +
            "JOIN user ON admin.user_id = user.id WHERE user_id = #{id}")
    Admin getById(int adminId);

    @Select("SELECT * FROM user JOIN admin ON user.id = admin.user_id " +
            "JOIN session ON admin.user_id = session.user_id " +
            "WHERE session.id = #{sessionId}")
    Admin getBySessionId(String sessionId);

    @Update("UPDATE admin JOIN user ON admin.user_id = user.id " +
            "SET firstName = #{firstName}, lastName = #{lastName}, patronymic = #{patronymic}, " +
            "position = #{position}, login = #{login}, password = #{password} " +
            "WHERE user_id = #{id}")
    void update(Admin admin);

    @Delete("DELETE FROM user WHERE id = #{id}")
    void delete(Admin admin);

}
