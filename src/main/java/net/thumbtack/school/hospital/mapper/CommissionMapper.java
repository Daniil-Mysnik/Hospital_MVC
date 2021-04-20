package net.thumbtack.school.hospital.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.time.LocalTime;

public interface CommissionMapper {

    @Insert("INSERT INTO commission (patient_id, ticket_id, date, time, duration) " +
            "VALUES (#{patientId}, #{ticketId}, #{date}, #{time}, #{duration})")
    @Options(useGeneratedKeys = true)
    void save(@Param("patientId") int patientId,
              @Param("ticketId") int ticketId,
              @Param("date") LocalDate date,
              @Param("time") LocalTime time,
              @Param("duration") int duration);

}
