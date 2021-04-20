package net.thumbtack.school.hospital.mapper;

import net.thumbtack.school.hospital.dto.response.TicketWithOneDoctorResponse;
import net.thumbtack.school.hospital.model.Patient;
import net.thumbtack.school.hospital.model.Ticket;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TicketMapper {

    @Insert("INSERT INTO ticket(number, patient_id) " +
            "VALUES(#{ticket.number}, #{patientId})")
    @Options(useGeneratedKeys = true, keyProperty = "ticket.id")
    int insert(@Param("ticket") Ticket ticket, @Param("patientId") int patientId);

    @Insert("INSERT INTO appointment_ticket(ticket_id, appointment_id) " +
            "VALUES(#{ticketId}, #{appointmentId})")
    void insertAppointmentTicket(@Param("ticketId") int ticketId, @Param("appointmentId") int appointmentId);
    
    @Select("SELECT patient_id FROM ticket WHERE number = #{ticketNumber}")
    Integer getPatientIdByTicket(String ticketNumber);

    @Select("SELECT DISTINCT number FROM appointment " +
            "JOIN appointment_ticket ON appointment_id = appointment.id " +
            "JOIN ticket ON ticket_id = ticket.id " +
            "WHERE patient_id = #{patientId}")
    List<String> getTicketsByPatientId(int patientId);

    @Select("SELECT *, ticket.number as ticket, room.number AS room FROM appointment " +
            "JOIN daySchedule ON appointment.schedule_id = daySchedule.id " +
            "JOIN appointment_ticket ON appointment_id = appointment.id " +
            "JOIN ticket ON ticket_id = ticket.id " +
            "JOIN doctor ON daySchedule.doctor_id = doctor.user_id " +
            "JOIN user ON user_id = user.id " +
            "JOIN speciality ON speciality_id = speciality.id " +
            "JOIN room ON doctor.room_id = room.id " +
            "WHERE ticket.number = #{ticketNumber} " +
            "GROUP BY login")
    @Results({
            @Result(property = "doctorId", column = "user_id"),
            @Result(property = "speciality", column = "name"),
    })
    List<TicketWithOneDoctorResponse> getAllInfoByTicket(String ticketNumber);

    @Select("SELECT DISTINCT ticket.number, patient_id, daySchedule.date FROM appointment " +
            "JOIN appointment_ticket ON appointment_id = appointment.id " +
            "JOIN ticket ON ticket_id = ticket.id " +
            "JOIN daySchedule ON schedule_id = daySchedule.id " +
            "WHERE doctor_id = #{doctorId} && date >= #{date} && state != 'FREE'")
    @Results({
            @Result(property = "patient", column = "patient_id", javaType = Patient.class,
                    one = @One(select = "net.thumbtack.school.hospital.mapper.PatientMapper.getById", fetchType = FetchType.EAGER)),
    })
    List<Ticket> getTicketByDoctorAndDate(@Param("doctorId") int doctorId, @Param("date") LocalDate date);

    @Select("SELECT patient_id, number FROM ticket " +
            "JOIN appointment_ticket ON ticket_id = ticket.id " +
            "JOIN appointment ON appointment.id = appointment_ticket.appointment_id " +
            "JOIN daySchedule ON daySchedule.id = appointment.schedule_id " +
            "JOIN doctor ON doctor.user_id = daySchedule.doctor_id " +
            "WHERE doctor_id = #{doctorId} && date = #{date} && time = #{time}")
    @Results({
            @Result(property = "patient", column = "patient_id", javaType = Patient.class,
                    one = @One(select = "net.thumbtack.school.hospital.mapper.PatientMapper.getById", fetchType = FetchType.EAGER)),
            @Result(property = "number", column = "ticket"),
    })
    Ticket getTicket(@Param("doctorId") int doctorId, @Param("date") LocalDate date, @Param("time") LocalTime time);

    @Select("SELECT commission.patient_id, commission.ticket_id, commission.date, commission.time FROM commission " +
            "JOIN ticket ON ticket.id = ticket_id " +
            "JOIN appointment_ticket ON appointment_ticket.ticket_id = ticket.id " +
            "JOIN appointment ON appointment.id = appointment_ticket.appointment_id " +
            "JOIN daySchedule ON daySchedule.id = appointment.schedule_id " +
            "JOIN doctor ON doctor.user_id = daySchedule.doctor_id " +
            "WHERE doctor_id = #{doctorId} && commission.date = #{date} && TIMEDIFF(commission.time, SEC_TO_TIME(appointment.duration*60)) <= #{time} && ADDTIME(commission.time, commission.duration) >= #{time}" +
            "LIMIT 1")
    @Results({
            @Result(property = "patient", column = "patient_id", javaType = Patient.class,
                    one = @One(select = "net.thumbtack.school.hospital.mapper.PatientMapper.getById", fetchType = FetchType.EAGER)),
            @Result(property = "number", column = "ticket"),
    })
    Ticket getCommissionTicket(@Param("doctorId") int doctorId, @Param("date") LocalDate date, @Param("time") LocalTime time);

    @Select("SELECT ticket.number, room.number AS room, daySchedule.date, appointment.time FROM ticket " +
            "JOIN appointment_ticket ON ticket_id = ticket.id " +
            "JOIN appointment ON appointment_id = appointment.id " +
            "JOIN daySchedule ON schedule_id = daySchedule.id " +
            "JOIN doctor ON doctor_id = doctor.user_id " +
            "JOIN room ON room_id = room.id " +
            "WHERE patient_id = #{patientId} && ticket.number LIKE 'D%' && date BETWEEN #{dateStart} AND #{dateEnd}")
    List<Ticket> getTicketsByTerm(@Param("patientId") int patientId, @Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);

    @Select("SELECT DISTINCT ticket.number, room.number AS room, commission.date, commission.time FROM ticket " +
            "JOIN appointment_ticket ON ticket_id = ticket.id " +
            "JOIN appointment ON appointment_id = appointment.id " +
            "JOIN commission ON commission.ticket_id = ticket.id " +
            "JOIN daySchedule ON schedule_id = daySchedule.id " +
            "JOIN doctor ON doctor_id = doctor.user_id " +
            "JOIN room ON room_id = room.id " +
            "WHERE commission.patient_id = #{patientId} && commission.date BETWEEN #{dateStart} AND #{dateEnd}")
    List<Ticket> getCommissionTicketsByTerm(@Param("patientId") int patientId, @Param("dateStart") LocalDate dateStart, @Param("dateEnd") LocalDate dateEnd);

    @Delete("DELETE FROM ticket WHERE number = #{ticketNumber}")
    void delete(String ticketNumber);

}
