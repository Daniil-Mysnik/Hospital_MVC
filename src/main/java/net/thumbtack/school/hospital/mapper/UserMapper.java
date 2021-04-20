package net.thumbtack.school.hospital.mapper;

import net.thumbtack.school.hospital.model.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {
    @Insert({"<script>" +
            "INSERT INTO user (firstName, lastName<if test='#{patronymic} != null'>, patronymic</if>, login, password, userType) " +
            "VALUES (#{firstName}, #{lastName}<if test='#{patronymic} != null'>, #{patronymic}</if>, #{login}, #{password}, #{userType})" +
            "</script>"})
    @Options(useGeneratedKeys = true, keyProperty = "id")
    void insert(User user);

    @Select("SELECT * FROM user WHERE id = #{id}")
    User getById(int id);

    @Select("SELECT user.id, firstName, lastName, patronymic, login, password, userType " +
            "FROM session JOIN user ON user_id = user.id WHERE session.id = #{sessionId}")
    User getBySessionId(String sessionId);

    @Select("SELECT * FROM user WHERE login = #{login}")
    User getByLogin(String login);

    @Delete("DELETE FROM user WHERE id != 1")
    void deleteAll();

}
