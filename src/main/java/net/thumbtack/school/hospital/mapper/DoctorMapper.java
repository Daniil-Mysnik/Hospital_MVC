package net.thumbtack.school.hospital.mapper;

import net.thumbtack.school.hospital.model.Doctor;
import net.thumbtack.school.hospital.model.User;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface DoctorMapper {
    @Insert("INSERT INTO doctor (user_id, speciality_id, room_id) VALUES (" +
            "(SELECT id FROM user WHERE login = #{login}), " +
            "(SELECT id FROM speciality WHERE name = #{speciality}), " +
            "(SELECT id FROM room WHERE number = #{room}))")
    void insert(Doctor doctor);

    @Select("SELECT * FROM doctor " +
            "JOIN user ON user_id = user.id " +
            "JOIN speciality ON speciality_id = speciality.id " +
            "JOIN room ON room_id = room.id " +
            "WHERE user.id = #{doctorId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "speciality", column = "name"),
            @Result(property = "room", column = "number"),
            @Result(property = "dayScheduleList", column = "id", javaType = List.class,
                many = @Many(select = "net.thumbtack.school.hospital.mapper.ScheduleMapper.getByDoctorId", fetchType = FetchType.EAGER))
    })
    Doctor getById(int doctorId);

    @Select("SELECT * FROM doctor " +
            "JOIN user ON user_id = user.id " +
            "JOIN speciality ON speciality_id = speciality.id " +
            "JOIN room ON room_id = room.id " +
            "WHERE name = #{speciality}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "speciality", column = "name"),
            @Result(property = "room", column = "number"),
            @Result(property = "dayScheduleList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.hospital.mapper.ScheduleMapper.getByDoctorId", fetchType = FetchType.EAGER))
    })
    List<Doctor> getBySpeciality(String speciality);

    @Select("SELECT * FROM doctor " +
            "JOIN user ON user_id = user.id " +
            "JOIN speciality ON speciality_id = speciality.id " +
            "JOIN room ON room_id = room.id")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "speciality", column = "name"),
            @Result(property = "room", column = "number"),
            @Result(property = "dayScheduleList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.hospital.mapper.ScheduleMapper.getByDoctorId", fetchType = FetchType.EAGER))
    })
    List<Doctor> getAll();

    @Select("SELECT user_id FROM doctor " +
            "JOIN speciality ON speciality_id = speciality.id " +
            "WHERE name = #{speciality}")
    List<Integer> getIdBySpeciality(String speciality);

    @Select("SELECT user_id FROM doctor")
    List<Integer> getAllId();

    @Select("SELECT name FROM speciality")
    List<String> getAllSpecialities();

    @Select("SELECT * FROM doctor " +
            "JOIN user ON user_id = user.id " +
            "JOIN speciality ON speciality_id = speciality.id " +
            "JOIN room ON room_id = room.id " +
            "JOIN daySchedule on daySchedule.doctor_id = doctor.user_id " +
            "JOIN appointment on schedule_id = daySchedule.id " +
            "WHERE name = #{speciality} && date = #{date} && time = #{time} && state = 'FREE' " +
            "ORDER BY RAND() " +
            "LIMIT 1")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "speciality", column = "name"),
            @Result(property = "room", column = "number"),
            @Result(property = "dayScheduleList", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.hospital.mapper.ScheduleMapper.getByDoctorId", fetchType = FetchType.EAGER))
    })
    Doctor getRandomBySpeciality(@Param("speciality") String speciality,
                                 @Param("date") LocalDate date,
                                 @Param("time") LocalTime time);

    @Select("SELECT number FROM room " +
            "JOIN doctor ON id = room_id " +
            "WHERE user_id = #{doctorId}")
    String getRoomByDoctorId(int doctorId);

    @Update("UPDATE doctor JOIN user ON user_id = user.id " +
            "JOIN speciality ON speciality_id = speciality.id " +
            "JOIN room ON room_id = room.id " +
            "SET firstName = #{firstName}, lastName = #{lastName}, patronymic = #{patronymic}, " +
            "login = #{login}, password = #{password}, " +
            "doctor.speciality_id = (select id from speciality where name = #{speciality}), " +
            "doctor.room_id = (select id from room where number = #{room}) " +
            "WHERE user.id = #{id}")
    void update(Doctor doctor);

    @Delete("DELETE FROM doctor WHERE user_id = #{id}")
    void delete(int id);

}
