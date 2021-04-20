package net.thumbtack.school.hospital.mapper;

import net.thumbtack.school.hospital.model.Session;
import net.thumbtack.school.hospital.model.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

public interface SessionMapper {
    @Insert("INSERT INTO session (id, user_id) " +
            "VALUES (#{sessionId}, #{user.id})")
    void insert(Session session);

    @Select("SELECT * FROM session WHERE id = #{id}")
    @Results({
            @Result(property = "sessionId", column = "id"),
            @Result(property = "user", column = "user_id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.hospital.mapper.UserMapper.getById", fetchType = FetchType.EAGER))
    })
    Session getById(String sessionId);

    @Select("SELECT id, user_id FROM session WHERE user_id = #{id}")
    @Results({
            @Result(property = "sessionId", column = "id"),
            @Result(property = "user", column = "user_id", javaType = User.class,
                    one = @One(select = "net.thumbtack.school.hospital.mapper.UserMapper.getById", fetchType = FetchType.EAGER))
    })
    Session getByUserId(int id);

    @Delete("DELETE FROM session WHERE id = #{sessionId}")
    void delete(Session session);

    @Delete("DELETE FROM session")
    void deleteAll();

}
