package net.thumbtack.school.hospital.mapper;

import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.DaySchedule;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.State;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface AppointmentMapper {
    @Insert("INSERT INTO appointment (schedule_id, time, duration, state) " +
            "VALUES (#{appointment.daySchedule.id}, #{appointment.time}, #{appointment.duration}, 'FREE')")
    @Options(useGeneratedKeys = true, keyProperty = "appointment.id")
    void save(@Param("appointment") Appointment appointment);

    @Insert({"<script>",
                "INSERT INTO appointment (schedule_id, time, duration, state) VALUES",
                "<foreach item='appointment' collection='appointments' separator=','>",
                    "( #{appointment.daySchedule.id}, #{appointment.time}, #{appointment.duration}, 'FREE')",
                "</foreach>",
            "</script>"})
    @Options(useGeneratedKeys = true)
    void batchInsert(@Param("appointments") List<Appointment> appointments);

    @Select("SELECT * FROM appointment " +
            "WHERE schedule_id = #{scheduleId}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "daySchedule", column = "schedule_id", javaType = DaySchedule.class,
                one = @One(select = "net.thumbtack.school.hospital.mapper.ScheduleMapper.getById", fetchType = FetchType.EAGER)),
    })
    Appointment getByIdSchedule(int scheduleId);

    @Select("SELECT appointment_id FROM appointment_ticket " +
            "JOIN ticket ON ticket.id = ticket_id " +
            "WHERE number = #{ticket}")
    List<Integer> getAppointmentIds(String ticket);

    @Select("SELECT * FROM appointment " +
            "JOIN daySchedule ON appointment.schedule_id = daySchedule.id " +
            "JOIN doctor ON daySchedule.doctor_id = doctor.user_id " +
            "WHERE date = #{date} && doctor_id = #{doctorId} " +
            "&& time > TIMEDIFF(#{timeStart}, SEC_TO_TIME(duration*60)) && time < #{timeEnd}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "daySchedule", column = "schedule_id", javaType = DaySchedule.class,
                    one = @One(select = "net.thumbtack.school.hospital.mapper.ScheduleMapper.getById", fetchType = FetchType.EAGER)),
            @Result(property = "patient", column = "patient_id", javaType = Patient.class,
                    one = @One(select = "net.thumbtack.school.hospital.mapper.PatientMapper.getById", fetchType = FetchType.EAGER)),
    })
    List<Appointment> getAppointmentsForCommission(@Param("date") LocalDate date,
                                                   @Param("doctorId") int doctorId,
                                                   @Param("timeStart") LocalTime timeStart,
                                                   @Param("timeEnd") LocalTime timeEnd);

    @Select("SELECT * FROM appointment " +
            "JOIN daySchedule ON appointment.schedule_id = daySchedule.id " +
            "WHERE doctor_id = #{doctorId} && date = #{date} && time = #{time}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "daySchedule", column = "schedule_id", javaType = DaySchedule.class,
                    one = @One(select = "net.thumbtack.school.hospital.mapper.ScheduleMapper.getById", fetchType = FetchType.EAGER)),
            @Result(property = "patient", column = "patient_id", javaType = Patient.class,
                    one = @One(select = "net.thumbtack.school.hospital.mapper.PatientMapper.getById", fetchType = FetchType.EAGER))
    })
    Appointment getAppointmentByTime(@Param("doctorId") int doctorId,
                                     @Param("date") LocalDate date,
                                     @Param("time") LocalTime time);

    @Update("UPDATE appointment " +
            "SET state = #{state} " +
            "WHERE id = #{appointmentId} && state = 'FREE'")
    int update(@Param("appointmentId") int appointmentId, @Param("state") State state);

    @Update("UPDATE appointment " +
            "SET state = 'FREE' " +
            "WHERE id = #{appointmentId}")
    int ridAppointment(@Param("appointmentId") int appointmentId);

}
