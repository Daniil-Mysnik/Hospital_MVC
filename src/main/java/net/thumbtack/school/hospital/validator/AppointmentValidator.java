package net.thumbtack.school.hospital.validator;

import net.thumbtack.school.hospital.exceptions.HospitalErrorCode;
import net.thumbtack.school.hospital.exceptions.HospitalException;
import net.thumbtack.school.hospital.model.Appointment;
import net.thumbtack.school.hospital.model.State;
import org.springframework.stereotype.Component;

@Component
public class AppointmentValidator {

    public void checkAppointmentFree(Appointment appointment) throws HospitalException {
        if (appointment.getState() != State.FREE) {
            throw new HospitalException(HospitalErrorCode.BUSY_TICKET);
        }
    }

}
