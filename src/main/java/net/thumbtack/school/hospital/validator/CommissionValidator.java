package net.thumbtack.school.hospital.validator;

import net.thumbtack.school.hospital.exceptions.HospitalErrorCode;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import org.springframework.stereotype.Component;

import java.time.LocalTime;
import java.util.List;

@Component
public class CommissionValidator {

    public void checkContainsRoom(List<String> rooms, String roomFromRequest) throws HospitalException {
        if (!rooms.contains(roomFromRequest)) {
            throw new HospitalException(HospitalErrorCode.WRONG_ROOM);
        }
    }

    public void checkAppointmentTimeBeforeWorking(LocalTime appointmentTime, LocalTime startWorkingTime) throws HospitalException {
        if (appointmentTime.isBefore(startWorkingTime)) {
            throw new HospitalException(HospitalErrorCode.NONWORKING_TIME);
        }
    }

    public void checkAppointmentTimeAfterWorking(LocalTime appointmentEndTime, int duration, LocalTime lastAppointmentTime) throws HospitalException {
        if (appointmentEndTime.minusMinutes(duration).isAfter(lastAppointmentTime)) {
            throw new HospitalException(HospitalErrorCode.NONWORKING_TIME);
        }
    }

}
