package net.thumbtack.school.hospital.mapper;

import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Doctor;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleMapper {
    @Insert("INSERT INTO daySchedule (doctor_id, date, timeStart, timeEnd) " +
            "VALUES (#{daySchedule.doctor.id}, #{daySchedule.date}, #{daySchedule.timeStart}, #{daySchedule.timeEnd})")
    @Options(useGeneratedKeys = true, keyProperty = "daySchedule.id")
    void save(@Param("daySchedule") DaySchedule daySchedule);

    @Insert({"<script>",
                "INSERT INTO daySchedule (doctor_id, date, timeStart, timeEnd) VALUES",
                "<foreach item='item' collection='list' separator=','>",
                        "( #{item.doctor.id}, #{item.date}, #{item.timeStart}, #{item.timeEnd})",
                "</foreach>",
            "</script>"})
    @Options(useGeneratedKeys = true)
    void batchInsert(@Param("list") List<DaySchedule> dayScheduleList);

    @Select("SELECT * FROM daySchedule WHERE id = #{id}")
    DaySchedule getById(int id);

    @Select("SELECT * FROM daySchedule WHERE doctor_id = #{doctorId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "doctor", column = "doctor_id", javaType = Doctor.class,
                one = @One(select = "net.thumbtack.school.hospital.mapper.DoctorMapper.getById", fetchType = FetchType.EAGER)),
            @Result(property = "appointments", column = "id", javaType = List.class,
                many = @Many(select = "net.thumbtack.school.hospital.mapper.AppointmentMapper.getByIdSchedule", fetchType = FetchType.EAGER))
    })
    List<DaySchedule> getByDoctorId(int doctorId);

    @Select("SELECT user_id FROM daySchedule " +
            "JOIN doctor ON doctor_id = user_id " +
            "JOIN room ON doctor.room_id = room.id " +
            "WHERE number = #{room}")
    List<Long> getSchedulesByRoom(String room);

    @Select("SELECT duration FROM appointment " +
            "JOIN daySchedule ON schedule_id = daySchedule.id " +
            "JOIN doctor ON doctor_id = user_id " +
            "WHERE doctor_id = #{doctorId} && date = #{date}" +
            "LIMIT 1")
    Integer getDuration(@Param(value = "doctorId") int doctorId, @Param(value = "date") LocalDate date);

    @Delete("DELETE FROM daySchedule WHERE id = #{scheduleId}")
    void delete(int scheduleId);

    @Delete("DELETE FROM daySchedule WHERE doctor_id = #{doctorId} && date >= #{date}")
    void deleteByDate(@Param(value = "doctorId") int doctorId, @Param(value = "date") LocalDate date);

}
